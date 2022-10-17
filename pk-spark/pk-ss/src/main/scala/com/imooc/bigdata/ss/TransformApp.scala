package com.imooc.bigdata.ss

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TransformApp {

  def main(args: Array[String]): Unit = {

    // 入口点
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")

    // 指定间隔5秒为一个批次
    val ssc = new StreamingContext(sparkConf,Seconds(5))


    // 这里的编程模型是RDD
    val data = List("pk")
    val dataRDD = ssc.sparkContext.parallelize(data).map(x => (x,true))


    // TODO... 对接网络数据
    val lines = ssc.socketTextStream("hadoop000", 9527)


    /**
      * 20221212,pk   => (pk , 20221212,pk )
      * 20221212,test
      */
    // 这里的编程模型是DStream   DStream join RDD
    lines.map(x => (x.split(",")(1), x))
      .transform(y => {
        y.leftOuterJoin(dataRDD)
          .filter(x => {
            x._2._2.getOrElse(false) != true
          }).map(x=>x._2._1)
      }).print()

    // 启动
    ssc.start()
    ssc.awaitTermination()

  }
}
