val rawUserArtistData = spark.read.textFile("hdfs:///dataset/lastfm/user_artist_data.txt")
rawUserArtistData.take(5).foreach(println)

val userArtistDF = rawUserArtistData.map { line =>
  val Array(user, artist, score) = line.split(' ')
  (user.toInt, artist.toInt)
}.toDF("user", "artist")

userArtistDF.agg(min("user"), max("user"), min("artist"), max("artist")).show

val rawArtistData = spark.read.textFile("hdfs:///dataset/lastfm/artist_data.txt")
rawArtistData.map {line =>
  val (id,name) = line.span(x => x != '\t')
  (id.toInt, name.trim)
}.count

val artistByID = rawArtistData.flatMap { line =>
  val (id, name)  = line.span(_ != '\t')
  if (name.isEmpty) {
    None
  } else {
    try {
      Some((id.toInt, name.trim))
    } catch {
      case _: NumberFormatException => None
    }
  }
}.toDF("id", "name")

val rawArtistAlias = spark.read.textFile("hdfs:///dataset/lastfm/artist_alias.txt")
val artistAlias = rawArtistAlias.flatMap { line =>
  val Array(artist, alias) = line.split('\t')
  if (artist.isEmpty) {
    None
  } else {
    Some((artist.toInt, alias.toInt))
  }
}.collect().toMap

artistAlias.head

artistByID.filter($"id".isin(1208690, 1003926)).show

import org.apache.spark.sql._
import org.apache.spark.broadcast._

def buildCounts (rawUserArtistData: Dataset[String],
    bArtistAlias: Broadcast[Map[Int, Int]]): DataFrame = {
  rawUserArtistData.map { line => 
    val Array(userID, artistID, count) = line.split(' ').map(_.toInt)
    val finalArtistID = bArtistAlias.value.getOrElse (artistID, artistID)
    (userID, finalArtistID, count)
  }.toDF("user", "artist", "count")
}

val bArtistAlias = sc.broadcast(artistAlias)

val trainData = buildCounts(rawUserArtistData, bArtistAlias)
trainData.cache

import org.apache.spark.ml.recommendation._
import scala.util.Random

val model = new ALS().
    setSeed(Random.nextLong()).
    setImplicitPrefs(true).
    setRank(10).
    setRegParam(0.01).
    setAlpha(1.0).
    setMaxIter(5).
    setUserCol("user").
    setItemCol("artist").
    setRatingCol("count").
    setPredictionCol("prediction").
    fit(trainData)

