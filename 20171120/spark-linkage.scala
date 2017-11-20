val rawblocks = sc.textFile("hdfs:///dataset/linkage")
rawblocks.count
val head = rawblocks.take(10)
head.foreach(println)
println(head)

def isHeader(line: String) = line.contains("id_1")
rawblocks.filter(isHeader)
rawblocks.filter(x => !isHeader(x)).take(10).foreach(println)

val noheader = rawblocks.filter( x => !isHeader(x))

val linkage = spark.read.csv("hdfs:///dataset/linkage")
linkage.show
linkage.printSchema

val linkage = spark.read.option("header", "true").option("nullValue", "?").option("inferSchema", "true").csv("hdfs:///dataset/linkage")
linkage.rdd.map(_.getAs[Boolean]("is_match")).countByValue

linkage.createOrReplaceTempView("linkage")
spark.sql("select is_match, count(*) as count from linkage group by is_match").show

val summary = linkage.describe()
summary.show

val matches = linkage.where("is_match = true")
val misses = linkage.where("is_match = false")

val matchSummary = matches.describe()
val missSummary = misses.describe()

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.first

def pivotSummary(desc: DataFrame): DataFrame = {
  val schema = desc.schema

  import desc.sparkSession.implicits._
  val longForm = desc.flatMap(row => {
    val metric = row.getString(0)
    (1 until row.size).map(i => {
      (metric, schema(i).name, row.getString(i).toDouble)
    })
  }).toDF("metric", "field", "value")

  longForm.groupBy("field").
    pivot("metric", Seq("count", "mean", "stddev", "min", "max")).
    agg(first("value"))
}

val matchSummaryT = pivotSummary(matchSummary)
val missSummaryT = pivotSummary(missSummary)

matchSummaryT.createOrReplaceTempView("match_desc")
missSummaryT.createOrReplaceTempView("miss_desc")

spark.sql("""
  SELECT a.field, a.count + b.count as total, a.mean - b.mean as delta
  FROM match_desc a INNER JOIN miss_desc b on a.field=b.field
  WHERE a.field NOT IN ("id_1", "id_2")
  ORDER BY delta DESC, total DESC
""").show

