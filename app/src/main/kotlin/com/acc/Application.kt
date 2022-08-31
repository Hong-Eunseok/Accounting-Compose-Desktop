package com.acc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.goodwill.presentation.main.Main
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
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
    val path = "./app/src/main/resource/goodwillstore.db"
    val url = "jdbc:sqlite:$path"
    println(url)
    Database.connect(url = url, driver = "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
}