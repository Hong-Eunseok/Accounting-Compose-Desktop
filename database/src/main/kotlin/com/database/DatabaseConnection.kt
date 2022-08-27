package com.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DatabaseConnection @Inject constructor() {

    private val databasePath = "../database/src/main/resources"
    private val databaseFileName = "acc.db"

    private var _connection: Connection? = null
    var connection: Connection
        get() = _connection ?: connectDatabase()
        set(value) {
            _connection = value
        }


    private fun connectDatabase() : Connection {
        try {
            val file = File(databasePath)
            println("efef ${file.absolutePath}")
            if (!file.exists()) {
                file.mkdir()
            }
            connection = DriverManager.getConnection("jdbc:sqlite:$databasePath/$databaseFileName")
        } catch (e: Exception) {
            println(e)
        }
        return connection ?: throw NullPointerException("Error")
    }
}