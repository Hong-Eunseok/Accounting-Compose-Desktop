package com.acc.goodwill.presentation.main

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.rememberContributor
import com.acc.goodwill.presentation.common.LocaleComposition
import com.acc.goodwill.presentation.common.SettingViewModel
import com.acc.goodwill.presentation.contributor.ContributorScreen
import com.acc.goodwill.presentation.donation.AddDonationScreen
import com.acc.goodwill.presentation.donation.DetailDonationScreen
import com.acc.goodwill.presentation.donation.DonationViewModel
import com.acc.goodwill.presentation.home.HomeScreen
import com.acc.goodwill.presentation.navigation.AddContributor
import com.acc.goodwill.presentation.navigation.AddDonationRoute
import com.acc.goodwill.presentation.navigation.DetailDonationRoute
import com.acc.goodwill.presentation.navigation.MainScreen
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

        val contributor = rememberContributor()
        var mainContributor by remember { mutableStateOf(Contributor.INIT) }
        var mainDonate by remember { mutableStateOf(Donate.INIT) }

        AppTheme(useDarkTheme = darkTheme) {
            CompositionLocalProvider(LocaleComposition provides selectedLocal) {
                when (route) {
                    MainScreen -> stateHolder.SaveableStateProvider(Unit) {
                        HomeScreen(
                            appComponent,
                            navigateAddDonation = { navigation.navigate(AddDonationRoute) },
                            navigateDetailDonation = {
                                mainDonate = it
                                navigation.navigate(DetailDonationRoute)
                            },
                            todayDonations
                        )
                    }
                    AddDonationRoute -> {
                        AddDonationScreen(appComponent).AddDonationScreen(
                            navigateBack = {
                                navigation.popLast()
                                donationViewModel.queryTodayDonation()
                            },
                            navigateAddContributor = {
                                contributor.init()
                                navigation.navigate(AddContributor)
                            },
                            navigateModifyContributor = {
                                contributor.copyContributor(it)
                                mainContributor = it
                                navigation.navigate(AddContributor)
                            }
                        )
                    }
                    AddContributor -> ContributorScreen(appComponent).ContributorScreen(
                        navigateBack = { navigation.popLast() },
                        contributor = contributor,
                        modifyContributor = if (mainContributor == Contributor.INIT) null else mainContributor,
                    )
                    DetailDonationRoute -> DetailDonationScreen(appComponent).DetailDonationScreen(
                        navigateBack = {
                            mainDonate = Donate.INIT
                            navigation.popLast()
                        },
                        mainDonate
                    )
                }

            }
        }
    }
}






