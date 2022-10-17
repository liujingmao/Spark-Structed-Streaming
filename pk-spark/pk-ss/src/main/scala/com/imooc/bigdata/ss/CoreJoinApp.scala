package com.imooc.bigdata.ss

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object CoreJoinApp {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[2]")
      .setAppName(this.getClass.getSimpleName)

    val sc = new SparkContext(sparkConf)

    // 要过滤掉的数据
    val list = new ListBuffer[(String,Boolean)]()
    list.append(("pk",true))
    val listRDD = sc.parallelize(list)


    // 准备数据
    val input = new ListBuffer[(String,String)]()
    input.append(("pk","20221212,pk"))
    input.append(("test","20221212,test"))
    val inputRDD  = sc.parallelize(input)

    val filterRDD = inputRDD.leftOuterJoin(listRDD)

    filterRDD.filter(x => {
      x._2._2.getOrElse(false) != true
    }).map(x=>x._2._1)
      .foreach(println)

    sc.stop()
  }
}
