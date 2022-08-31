package com.acc.goodwill.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.goodwill.presentation.common.LocaleComposition
import com.acc.goodwill.presentation.common.SettingViewModel
import com.acc.goodwill.presentation.donation.AddDonationContent
import com.acc.goodwill.presentation.home.HomeScreen
import com.acc.goodwill.presentation.navigation.AddDonationScreen
import com.acc.goodwill.presentation.navigation.MainScreen
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

                    }
                }

            }
        }
    }
}

