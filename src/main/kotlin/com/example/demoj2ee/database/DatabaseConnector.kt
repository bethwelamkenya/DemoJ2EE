package com.example.demoj2ee.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseConnector {
    private val sqlConnection: String =
        "jdbc:sqlite:" + System.getProperty("user.home") + "/AppData/Roaming/App2/Databases/church.sqlite"

//    private val sqlConn: String = "jdbc:mysql://localhost:3333/church?useSSL=false"
    private val sqlConn: String = "jdbc:mysql://localhost:3333/church"
    private val userName: String = "root"
    private val password: String = "9852"

    //            Class.forName("com.mysql.jdbc.driver")
//            return DriverManager.getConnection(sqlConnection, userName, password)
    fun getConnection(): Connection {
        try {
//            Class.forName("org.sqlite.JDBC")
            Class.forName("com.mysql.jdbc.driver")
//            return DriverManager.getConnection(sqlConnection)
            return DriverManager.getConnection(sqlConn, userName, password)

        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}