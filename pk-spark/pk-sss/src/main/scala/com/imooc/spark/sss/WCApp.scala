package com.imooc.spark.sss

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, SparkSession}

object WCApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[2]")
      .appName(this.getClass.getName).getOrCreate()

    import spark.implicits._

    val lines = spark.readStream.format("socket")
      .option("host", "hadoop000")
      .option("port", 9999)
      .load()

    val wordCount = lines.as[String]
      .flatMap(_.split(","))
      .createOrReplaceTempView("wc")

    spark.sql(
      """
        |select
        |value, count(1) as cnt
        |from
        |wc
        |group by value
      """.stripMargin)
      .writeStream
        .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }
}
