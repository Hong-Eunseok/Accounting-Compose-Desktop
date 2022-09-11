package com.acc.goodwill.data.source.presentation.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.donation.DonationContent
import com.acc.goodwill.data.source.presentation.navigation.*
import com.acc.goodwill.data.source.presentation.setting.SettingScreen
import com.acc.goodwill.domain.model.Donate
import com.navigation.rememberNavigation

@Composable
fun HomeScreen(
    appComponent: AppComponent,
    navigateAddDonation: () -> Unit,
    navigateDetailDonation: (Donate) -> Unit,
    todayDonations: List<Donate>
) {

    val navigation = rememberNavigation(defaultRoute = HomeDonation)
    val route by navigation.routeStack.collectAsState()

    println("HomeScreen size : ${todayDonations.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "수원굿윌스토어", style = MaterialTheme.typography.h3) },
                actions = {
                    IconButton(onClick = { navigation.navigate(HomeSetting) }) { AppIcon(imageVector = Icons.Default.Settings) }
                }
            )
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val screenWidth = constraints.maxWidth
            Row(Modifier.fillMaxWidth()) {
                // menu side
                HomeMenu(
                    route,
                    screenWidth.dp,
                    navigateStatistics = { navigation.navigate(HomeStatistics) },
                    navigateDonation = { navigation.navigate(HomeDonation) },
                    navigateSearch = { navigation.navigate(HomeSearch) },
                    navigateReport = { navigation.navigate(HomeReport) }
                )
                // content side
                HomeContent(modifier = Modifier.weight(3f)) {
                    when (route) {
                        HomeDonation -> DonationContent(
                            navigateDetailDonation = navigateDetailDonation,
                            navigateAddDonation = navigateAddDonation,
                            todayDonations = todayDonations
                        )
                        HomeSearch -> EmptyScreen()
                        HomeStatistics -> EmptyScreen()
                        HomeReport -> EmptyScreen()
                        HomeSetting -> SettingScreen(appComponent).SettingsScreen { navigation.popLast() }
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyScreen() {
    Scaffold {

    }
}