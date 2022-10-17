package com.imooc.bigdata.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQLApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[2]")
      .getOrCreate()


    val df = spark.read.format("json").load("pk-ss/data/emp.json")

//    df.printSchema()
//    df.show()

    // TODO... API & SQL

    df.createOrReplaceTempView("emp")
    spark.sql(
      """
        |
        |select
        |name,salary
        |from
        |emp
        |where salary > 4000
        |
      """.stripMargin).show(false)

    spark.stop()

  }

}
