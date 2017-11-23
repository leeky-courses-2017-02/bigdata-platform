// Option collection
val countries = Map(
  "USA" -> "Washington",
  "UK" -> "London",
  "South Korea" -> "Seoul"
)

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

