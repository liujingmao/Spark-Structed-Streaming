package com.imooc.bigdata.ss

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 完成基于SS进行词频统计分析
  * 数据源：HDFS
  *
  * SS的编程范式：
  * 1）ssc  <==  sparkConf
  * 2) 业务逻辑
  * 3) 启动流作业
  * */
object FileWordCountApp {

  def main(args: Array[String]): Unit = {

    // 入口点
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")

    // 指定间隔5秒为一个批次
    val ssc = new StreamingContext(sparkConf,Seconds(5))


    // TODO... 对接网络数据
    val lines = ssc.textFileStream("file:///Users/rocky/IdeaProjects/imooc-workspace/tmp/ss")


    // TODO... 业务逻辑处理
    // 输入数据以逗号分隔  // y = f(x)
    val result = lines.flatMap(_.split(",")).map((_, 1))
      .reduceByKey(_ + _)


    result.print()

    // 启动
    ssc.start()
    ssc.awaitTermination()

  }

}
