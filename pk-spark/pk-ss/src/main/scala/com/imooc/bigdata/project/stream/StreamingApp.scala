package com.imooc.bigdata.project.stream

import com.imooc.bigdata.project.utils.{DateUtils, HBaseClient}
import org.apache.hadoop.hbase.client.Table
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer


object StreamingApp {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      //.setAppName(this.getClass.getSimpleName)
      //.setMaster("local[2]")
    //sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //sparkConf.set("spark.streaming.kafka.maxRatePerPartition","100")

    val ssc = new StreamingContext(sparkConf, Seconds(10)) // new context

    val groupId = "pk-spark-group-1"
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop000:9092,hadoop000:9093,hadoop000:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("access-topic-prod")

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )



    /**
      * 日期
      * 时长
      * 用户名
      */
    val logStream = stream.map(x => {
      val splits = x.value().split("\t")
      // 时间 时长 用户 ==> 时间+用户 时长 ==> wc的变种
      (DateUtils.parseToHour(splits(0).trim), splits(1).toLong, splits(5).trim)
    })


    /**
      * 统计结果 ==> DB
      *
      * 每个用户每个小时使用时长
      * 用户，每个小时，时长
      */
    logStream.map(x => {
      ((x._1,x._3), x._2)
    }).reduceByKey(_+_)
      .foreachRDD(rdd => {
        rdd.foreachPartition(partition => {
          val table = HBaseClient.getTable("access_user_hour")
          partition.foreach(x => {
            table.incrementColumnValue(
              (x._1._1+"_"+x._1._2).getBytes,
              "o".getBytes,
              "time".getBytes,
              x._2
            )
          })
          table.close()
        })
      })

        logStream.map(x => {
          ((x._1.substring(0,8),x._3), x._2)
        }).reduceByKey(_+_)
          .foreachRDD(rdd => {
            rdd.foreachPartition(partition => {
              val table = HBaseClient.getTable("access_user_day")
              partition.foreach(x => {
                table.incrementColumnValue(
                  (x._1._1+"_"+x._1._2).getBytes,
                  "o".getBytes,
                  "time".getBytes,
                  x._2
                )
              })
              table.close()
            })
          })

    ssc.start()
    ssc.awaitTermination()
  }

}
