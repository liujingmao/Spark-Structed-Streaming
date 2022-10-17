package com.imooc.bigdata.project.utils

import java.util

import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

object HBaseClient {


  private val configuration = HBaseConfiguration.create()
  configuration.set("hbase.zookeeper.quorum","hadoop000:2181")

  private val connection = ConnectionFactory.createConnection(configuration)

  def main(args: Array[String]): Unit = {

//    println(getTable("user"))
//    insert()
    query2()
  }

  def getTable(tableName:String) = {
    connection.getTable(TableName.valueOf(tableName))
  }

  def query(): Unit = {
    val table = connection.getTable(TableName.valueOf("user"))

    val scan = new Scan()
    scan.setStartRow("1".getBytes)
    scan.setStopRow("4".getBytes())
    scan.addColumn("o".getBytes(),"name".getBytes())

    val iterator = table.getScanner(scan).iterator()
    while(iterator.hasNext) {
      val result: Result = iterator.next()

      while(result.advance()) {
        val cell: Cell = result.current()
        val row  = new String(CellUtil.cloneRow(cell))
        val cf = new String(CellUtil.cloneFamily(cell))
        val qualifier = new String(CellUtil.cloneQualifier(cell))
        val value = new String(CellUtil.cloneValue(cell))


        println(row + "..." + cf + "..." + qualifier+ "..." + value )
      }

    }


    if(null != table) {
      table.close()
    }
  }

  def query2(): Unit = {
    val table = connection.getTable(TableName.valueOf("access_user_day"))

    val scan = new Scan()
    scan.setStartRow("2020081013".getBytes)
    scan.setStopRow("2020082418".getBytes())

    val iterator = table.getScanner(scan).iterator()
    while(iterator.hasNext) {
      val result: Result = iterator.next()

      while(result.advance()) {
        val cell: Cell = result.current()
        val row  = new String(CellUtil.cloneRow(cell))
        val cf = new String(CellUtil.cloneFamily(cell))
        val qualifier = new String(CellUtil.cloneQualifier(cell))

        val value = Bytes.toLong(CellUtil.cloneValue(cell))
        println(row + "..." + cf + "..." + qualifier+ "..." + value )
      }

    }


    if(null != table) {
      table.close()
    }
  }

  def insert(): Unit = {
    val table = connection.getTable(TableName.valueOf("user"))

    val put = new Put("4".getBytes)
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("name"),Bytes.toBytes("zhangsan"))
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("age"),Bytes.toBytes("20"))
    put.addColumn(Bytes.toBytes("o"),Bytes.toBytes("sex"),Bytes.toBytes("f"))

    table.put(put)

    if(null != table) {
      table.close()
    }
  }

}
