package com.imooc.bigdata.offset

trait OffsetsManager {

  // 入参：offsetRanges和groupid
  def storeOffsets()

  //入参：groupid和topic
  def obtainOffsets()

}
