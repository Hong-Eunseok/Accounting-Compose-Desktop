package com.acc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.goodwill.presentation.main.Main

fun main() {
    val appComponent: AppComponent = DaggerAppComponent.create()
    application {
        Window(onCloseRequest = ::exitApplication) {
            Main(appComponent).Main()
        }
    }
}
