package com.imooc.bigdata.offset

import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

object ScalikeJDBCApp {
  def main(args: Array[String]): Unit = {

    DBs.setupAll()
    query()
    println("-------")
    insert()
    println("-------")
    query()
  }

  def insert (): Unit = {
    DB.autoCommit { implicit session =>
      SQL("insert into offsets_storage(topic,groupid,partitions,offset) values(?,?,?,?)")
        .bind("pktest","test-group", 3,99)
        .update().apply()
    }
  }

  def update (): Unit = {
    DB.autoCommit { implicit session =>
      SQL("update offsets_storage set offset=? where topic=? and groupid=? and partitions=?")
        .bind(29, "pktest","test-group", 0)
        .update().apply()
    }
  }

  def query(): Unit ={
//    DB.readOnly{ implicit session =>
//      SQL("select * from offsets_storage")
//        .map(rs => rs.long("offset"))
//        .list().apply()
//    }.foreach(println)


    DB.readOnly{ implicit session =>
      SQL("select * from offsets_storage")
        .map(rs => Offset(
          rs.string("topic"),
          rs.string("groupid"),
          rs.int("partitions"),
          rs.long("offset")
        ))
        .list().apply()
    }.foreach(println)

  }

}

case class Offset(topic:String, groupId:String, partitionId:Int, offset:Long)
