package com.acc.goodwill.data.source.presentation.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.donation.DonationContent
import com.acc.goodwill.data.source.presentation.donation.DonationViewModel
import com.acc.goodwill.data.source.presentation.navigation.Statistics
import com.acc.goodwill.data.source.presentation.navigation.Donation
import com.acc.goodwill.data.source.presentation.navigation.Report
import com.acc.goodwill.data.source.presentation.navigation.Search
import com.navigation.rememberNavigation
import javax.inject.Inject

class HomeScreen(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
        viewModel.queryTodayDonation()

    }

    @Composable
    fun HomeScreen(navigateAddDonation: () -> Unit) {

        val navigation = rememberNavigation(defaultRoute = Donation)
        val route by navigation.routeStack.collectAsState()
        val todayDonate by viewModel.todayDonation.collectAsState()

        println("HomeScreen size : ${todayDonate.size}")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "수원굿윌스토어", style = MaterialTheme.typography.h3) },
                    actions = {
                        IconButton(onClick = { println("setting click Icon!!!") }) { AppIcon(imageVector = Icons.Default.Settings) }
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
                        navigateStatistics = { navigation.navigate(Statistics) },
                        navigateDonation = { navigation.navigate(Donation) },
                        navigateSearch = { navigation.navigate(Search) },
                        navigateReport = { navigation.navigate(Report) }
                    )
                    // content side
                    HomeContent(modifier = Modifier.weight(3f)) {
                        when (route) {
                            Donation -> DonationContent(navigateAddDonation = navigateAddDonation)
                            Search -> EmptyScreen()
                            Statistics -> EmptyScreen()
                            Report -> EmptyScreen()
                        }
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