package com.headstrongpro.desktop.core.connection

import org.json.simple.JSONObject
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * DB Context
 */
class DBContext : Configurable() {

    private var url = ""
    private var username = ""
    private var password = ""

    init {
        val list = config
        url = list[0] as String
        username = list[1] as String
        password = list[2] as String
    }

    private fun connect(hostname: String, user: String, pass: String): Connection = DriverManager.getConnection(hostname, user, pass)

    fun getConnection(): Connection = connect(url, username, password)

    fun getFromDataBase(query: String): ResultSet = getConnection().createStatement().executeQuery(query)

    fun upload(query: String): Unit {
        val con = getConnection()
        con.createStatement().execute(query)
        con.close()
    }

    fun uploadSafe(stmt: PreparedStatement): Int = stmt.executeUpdate()

    override fun getConfig(): MutableList<Any> {
        val credentials: JSONObject = parseJsonConfig("/config.json", "database_mssql")
        val list = mutableListOf<Any>()
        credentials["url"]?.let { list.add(it) }
        credentials["user"]?.let { list.add(it) }
        credentials["pass"]?.let { list.add(it) }
        return list
    }
}