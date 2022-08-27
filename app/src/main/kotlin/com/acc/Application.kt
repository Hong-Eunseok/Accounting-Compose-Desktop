package com.acc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.features.main.ui.Main


fun main() = application {
    val appComponent: AppComponent = DaggerAppComponent.create()
    Window(onCloseRequest = ::exitApplication) {
        Main(appComponent).Main()
    }
}
