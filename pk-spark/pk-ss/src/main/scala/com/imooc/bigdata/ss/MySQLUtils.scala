package com.imooc.bigdata.ss

import java.sql.{Connection, DriverManager}


object MySQLUtils {

  def getConnection() = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://hadoop000:3306/pk","root","root")
  }


  def close(connection:Connection): Unit = {
    if(null != connection) {
      connection.close()
    }
  }
}
