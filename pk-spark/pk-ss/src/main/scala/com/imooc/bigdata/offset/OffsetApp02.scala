package com.imooc.bigdata.offset

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}


object OffsetApp02 {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")

    val ssc = new StreamingContext(sparkConf, Seconds(10)) // new context


    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop000:9092,hadoop000:9093,hadoop000:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "pk-spark-group-1",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("my-replicated-topic")

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
