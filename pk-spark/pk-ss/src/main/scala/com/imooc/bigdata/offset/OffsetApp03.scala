package com.imooc.bigdata.offset

import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}
import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs


object OffsetApp03 {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")

    val ssc = new StreamingContext(sparkConf, Seconds(10)) // new context

    DBs.setupAll()


    val groupId = "pk-spark-group-1"
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop000:9092,hadoop000:9093,hadoop000:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("my-replicated-topic")

    // TODO... 从存储介质中获取到已经处理过的offset

    val offsets: collection.Map[TopicPartition, Long] =
      DB.readOnly { implicit session => {
      SQL("select * from offsets_storage where groupid=? and topic=?")
        .bind(groupId, topics.head)
        .map(rs => {
          (new TopicPartition(rs.string("topic"), rs.int("partitions")), rs.long("offset"))
        }).list().apply
    }
    }.toMap

//    for((k,v) <- offsets) {
//      println(k, v)
//    }


    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams,offsets)
    )

    stream.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd.foreachPartition { iter =>
          val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
          println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        }

        // TODO... 业务逻辑

        // TODO... 保存offset到某个存储介质中去
        offsetRanges.map(x => {
          DB.autoCommit{ implicit session => {
            SQL(
              """
                |insert into offsets_storage(topic,groupid,partitions,offset) values(?,?,?,?)
                |on duplicate key update offset=?
              """.stripMargin)
              .bind(x.topic,groupId, x.partition,x.untilOffset,x.untilOffset)
              .update().apply
          }
          }
        })
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
