package com.imooc.spark.sss.project

import java.sql.Timestamp

import com.imooc.spark.sss.SourceApp.eventTimeWindow
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.streaming.OutputMode
import redis.clients.jedis.Jedis

object SSSApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
//      .master("local[2]")
//        .config("spark.sql.shuffle.partitions","10")
//      .appName(this.getClass.getName)
      .getOrCreate()



    // TODO... 从已经保存过的offset中获取

    import spark.implicits._
    spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "hadoop000:9092,hadoop000:9093,hadoop000:9094")
      .option("subscribe", "access-topic-prod")
      //.option("startingOffsets", """{"access-topic-prod":{"0":60000}}""")
      .load()
      .selectExpr("CAST(value AS STRING)")
      .as[String].map(x => {
      val splits = x.split("\t")
      val time = splits(0)
      val ip = splits(2)

      (new Timestamp(time.toLong), DateUtils.parseToDay(time), IPUtils.parseIP(ip))
    }).toDF("ts","day","province")
        .withWatermark("ts","10 minutes")
        .groupBy("day","province")
      .count()
      .writeStream
      //.format("console") // 这的console操作是结果显示在控制台 ==> Redis
      .outputMode(OutputMode.Update())
        .foreach(new ForeachWriter[Row] {
          var client:Jedis = _
          override def process(value: Row): Unit = {
            val day = value.getString(0)
            val province = value.getString(1)
            val cnts = value.getLong(2)
//            val offset = value.getAs[String]("offset")
//            client.set("","")
            client.hset("day-province-cnts-"+day, province, cnts+"")
          }

          override def close(errorOrNull: Throwable): Unit = {
            if(null != client) {
              RedisUtils.returnResource(client)
            }
          }

          override def open(partitionId: Long, epochId: Long): Boolean = {
            client = RedisUtils.getJedisClient

            client != null
          }
        })
        .option("checkpointLocation","./chk")
      .start()
      .awaitTermination()
  }

}
