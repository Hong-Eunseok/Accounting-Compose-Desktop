package com.acc

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.features.main.ui.Main
import com.acc.goodwill.data.source.table.ContributorTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection


fun main() {
    val test = true
    initializeConnection()
    val appComponent: AppComponent = DaggerAppComponent.create()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 1000.dp, height = 1000.dp)
        ) {
            if (test) {
                com.acc.goodwill.data.source.presentation.main.Main(appComponent).Main()
            } else {
                Main(appComponent).Main()
            }
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
        SchemaUtils.createMissingTablesAndColumns(ContributorTable)
    }
}