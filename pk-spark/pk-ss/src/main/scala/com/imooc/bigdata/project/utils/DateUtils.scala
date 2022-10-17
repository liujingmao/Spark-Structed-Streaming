package com.imooc.bigdata.project.utils

import org.apache.commons.lang3.time.FastDateFormat

object DateUtils {

  // 1597039092628
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyyMMddHH")


  def parseToHour(time:String) = {
    TARGET_FORMAT.format(time.toLong)
  }

  def main(args: Array[String]): Unit = {
    println(parseToHour("1597039092628"))
  }

}
