package com.acc.goodwill.data.source.presentation.main

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.common.SettingViewModel
import com.acc.goodwill.data.source.presentation.contributor.ContributorScreen
import com.acc.goodwill.data.source.presentation.donation.AddDonationScreen
import com.acc.goodwill.data.source.presentation.donation.DonationViewModel
import com.acc.goodwill.data.source.presentation.home.HomeScreen
import com.acc.goodwill.data.source.presentation.navigation.AddContributor
import com.acc.goodwill.data.source.presentation.navigation.AddDonationRoute
import com.acc.goodwill.data.source.presentation.navigation.MainScreen
import com.acc.goodwill.domain.model.Contributor
import com.navigation.rememberNavigation
import javax.inject.Inject

class Main(private val appComponent: AppComponent) {

    @Inject lateinit var settingViewModel: SettingViewModel
    @Inject lateinit var donationViewModel: DonationViewModel

    init {
        appComponent.inject(this)
        donationViewModel.queryTodayDonation()
    }

    @Composable
    fun Main() {
        val stateHolder = rememberSaveableStateHolder()
        val darkTheme by settingViewModel.darkTheme.collectAsState()
        val selectedLocal by settingViewModel.selectedLocal.collectAsState()
        // TODO : Changed decompose library but it occur error from font exception
        val navigation = rememberNavigation(defaultRoute = MainScreen)
        val route by navigation.routeStack.collectAsState()

        val todayDonations by donationViewModel.todayDonation.collectAsState()

        var modifyContributor by remember { mutableStateOf(Contributor.INIT) }

        AppTheme(useDarkTheme = darkTheme) {
            CompositionLocalProvider(LocaleComposition provides selectedLocal) {
                when (route) {
                    MainScreen -> stateHolder.SaveableStateProvider(Unit) {
                        HomeScreen(
                            navigateAddDonation = { navigation.navigate(AddDonationRoute) },
                            todayDonations
                        )
                    }
                    AddDonationRoute -> {
                        AddDonationScreen(appComponent).AddDonationScreen(
                            navigateBack = {
                                navigation.popLast()
                                donationViewModel.queryTodayDonation()
                            },
                            navigateAddContributor = { navigation.navigate(AddContributor) },
                            navigateModifyContributor = {
                                modifyContributor = it
                                navigation.navigate(AddContributor)
                            }
                        )
                    }
                    AddContributor -> ContributorScreen(appComponent).ContributorScreen(
                        navigateBack = { navigation.popLast() },
                        modifyContributor = if (modifyContributor == Contributor.INIT) null else modifyContributor
                    )
                }

            }
        }
    }
}






