package com.acc.goodwill.data.source.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.common.SettingViewModel
import com.acc.goodwill.data.source.presentation.donation.AddDonationContent
import com.acc.goodwill.data.source.presentation.home.HomeScreen
import com.acc.goodwill.data.source.presentation.navigation.AddDonationScreen
import com.acc.goodwill.data.source.presentation.navigation.MainScreen
import com.navigation.rememberNavigation
import javax.inject.Inject

class Main(appComponent: AppComponent) {

    @Inject lateinit var settingViewModel: SettingViewModel

    private val addDonationContent by lazy { AddDonationContent(appComponent) }

    init {
        appComponent.inject(this)
    }

    @Composable
    fun Main() {
        val stateHolder = rememberSaveableStateHolder()
        val darkTheme by settingViewModel.darkTheme.collectAsState()
        val selectedLocal by settingViewModel.selectedLocal.collectAsState()
        // TODO : Changed decompose library but it occur error from font exception
        val navigation = rememberNavigation(defaultRoute = MainScreen)
        val route by navigation.routeStack.collectAsState()

        AppTheme(useDarkTheme = darkTheme) {
            CompositionLocalProvider(LocaleComposition provides selectedLocal) {
                when (route) {
                    MainScreen -> stateHolder.SaveableStateProvider(Unit) {
                        HomeScreen(navigateAddDonation = { navigation.navigate(AddDonationScreen) })
                    }
                    AddDonationScreen -> {
                        addDonationContent.addDonationContent { navigation.popLast() }
                    }
                }

            }
        }
    }
}






