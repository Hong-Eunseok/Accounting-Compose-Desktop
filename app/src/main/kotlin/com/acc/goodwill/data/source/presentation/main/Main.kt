package com.acc.goodwill.data.source.presentation.main

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.common.SettingViewModel
import com.acc.goodwill.data.source.presentation.donation.AddContributorScreen
import com.acc.goodwill.data.source.presentation.donation.AddDonationScreen
import com.acc.goodwill.data.source.presentation.home.HomeScreen
import com.acc.goodwill.data.source.presentation.navigation.AddContributor
import com.acc.goodwill.data.source.presentation.navigation.AddDonationRoute
import com.acc.goodwill.data.source.presentation.navigation.MainScreen
import com.navigation.rememberNavigation
import javax.inject.Inject

class Main(private val appComponent: AppComponent) {

    @Inject lateinit var settingViewModel: SettingViewModel

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
                        HomeScreen(navigateAddDonation = { navigation.navigate(AddDonationRoute) })
                    }
                    AddDonationRoute -> {
                        AddDonationScreen(appComponent).AddDonationScreen(
                            navigateBack = { navigation.popLast() },
                            navigateAddContributor = { navigation.navigate(AddContributor) }
                        )
                    }
                    AddContributor -> AddContributorScreen(appComponent).AddContributorScreen { navigation.popLast() }
                }

            }
        }
    }
}






