package com.imooc.spark.sss

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.functions.window

object SourceApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[2]")
      .appName(this.getClass.getName).getOrCreate()


    eventTimeWindow(spark)


  }

  def readCsv(spark:SparkSession): Unit = {

    val userSchema = new StructType()
      .add("id", IntegerType)
      .add("name",StringType)
      .add("city", StringType)

    spark.readStream
      .format("csv")
      .schema(userSchema)
      .load("pk-sss/data/csv")
        .groupBy("city")
        .count()
      .writeStream
      .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }


  def readCsvPartition(spark:SparkSession): Unit = {

    val userSchema = new StructType()
      .add("id", IntegerType)
      .add("name",StringType)
      .add("city", StringType)

    spark.readStream
      .format("csv")
      .schema(userSchema)
      .load("pk-sss/data/partition")
      .writeStream
      .format("console")
      .start()
      .awaitTermination()
  }

  def kafkaSource(spark:SparkSession): Unit = {
    import spark.implicits._
    spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "hadoop000:9092")
      .option("subscribe", "ssstopic")
      .load()
      .selectExpr("CAST(value AS STRING)")
        .as[String].flatMap(_.split(","))
        .groupBy("value").count()
      .writeStream
      .format("console")
        .outputMode(OutputMode.Update())
      .start()
      .awaitTermination()
  }


  def eventTimeWindow(spark:SparkSession): Unit = {
    import spark.implicits._
    spark.readStream.format("socket")
      .option("host","hadoop000")
      .option("port",9999)
      .load.as[String]
        .map(x => {
          val splits = x.split(",")
          (splits(0),splits(1))
        }).toDF("ts", "word")
        .groupBy(
          window($"ts", "10 minutes", "5 minutes"),
          $"word"
        ).count()
        .sort("window")
      .writeStream
      .format("console")
        .option("truncate","false")
      .outputMode(OutputMode.Complete())
      .start()
      .awaitTermination()
  }
}
