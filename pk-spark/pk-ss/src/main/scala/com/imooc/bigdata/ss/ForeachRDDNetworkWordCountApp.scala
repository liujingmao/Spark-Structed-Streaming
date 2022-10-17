package com.imooc.bigdata.ss


import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 完成基于SS进行词频统计分析，并把结果写入到MySQL
  * 数据源：nc
  *
  * SS的编程范式：
  * 1）ssc  <==  sparkConf
  * 2) 业务逻辑
  * 3) 启动流作业
  * */
object ForeachRDDNetworkWordCountApp {

  def main(args: Array[String]): Unit = {

    // 入口点
    val sparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")

    // 指定间隔5秒为一个批次
    val ssc = new StreamingContext(sparkConf,Seconds(5))


    // TODO... 对接网络数据
    val lines = ssc.socketTextStream("hadoop000", 9527)



    // TODO... 业务逻辑处理
    // 输入数据以逗号分隔
    val result = lines.flatMap(_.split(",")).map((_, 1))
      .reduceByKey(_ + _)


    // TODO... 把结果通过foreachRDD算子输出到MySQL中
    result.foreachRDD(rdd => {
      rdd.foreachPartition(partition => {

        val connection = MySQLUtils.getConnection()

        partition.foreach(pair => {
          val sql = s"insert into wc(word,cnt) values('${pair._1}',${pair._2})"
          connection.createStatement().execute(sql)
        })

        MySQLUtils.close(connection)
      })
    })


    // 启动
    ssc.start()
    ssc.awaitTermination()

  }

}
