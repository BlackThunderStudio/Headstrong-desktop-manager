package com.headstrongpro.desktop.core.connection

import org.json.simple.JSONObject
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 *
 * desktop-manager
 *
 *
 * Created by rajmu on 17.05.29.
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
        val creds: JSONObject = parseJsonConfig("/config.json", "database_mssql")
        val list = mutableListOf<Any>()
        creds["url"]?.let { list.add(it) }
        creds["user"]?.let { list.add(it) }
        creds["pass"]?.let { list.add(it) }
        return list
    }
}