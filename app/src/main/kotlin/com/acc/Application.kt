package com.acc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.goodwill.data.source.table.User
import com.acc.goodwill.presentation.main.Main
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

fun main() {
    initializeConnection()
    val appComponent: AppComponent = DaggerAppComponent.create()
    application {
        Window(onCloseRequest = ::exitApplication) {
            Main(appComponent).Main()
        }
    }
}

private fun initializeConnection() {
    val db = "goodwillstore.db"
    val folder = "./app/src/main/resource/"
    File(folder).mkdirs()
    val connect = Database.connect(url = "jdbc:sqlite:$folder$db", driver = "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction(connect) {
        SchemaUtils.createMissingTablesAndColumns(User)
    }
}