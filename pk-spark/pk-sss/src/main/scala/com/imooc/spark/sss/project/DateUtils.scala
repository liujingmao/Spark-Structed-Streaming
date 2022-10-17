package com.imooc.spark.sss.project

import org.apache.commons.lang3.time.FastDateFormat

object DateUtils {

  // 1597039092628
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyyMMdd")


  def parseToDay(time:String) = {
    TARGET_FORMAT.format(time.toLong)
  }

  def main(args: Array[String]): Unit = {
    println(parseToDay("1597039092628"))
  }

}
