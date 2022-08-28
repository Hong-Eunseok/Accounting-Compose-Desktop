package com.acc.goodwill.presentation.home

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
import com.acc.goodwill.presentation.donation.DonationContent
import com.acc.goodwill.presentation.navigation.Dashboard
import com.acc.goodwill.presentation.navigation.Donation
import com.acc.goodwill.presentation.navigation.Report
import com.acc.goodwill.presentation.navigation.Search
import com.navigation.rememberNavigation

@Composable
fun HomeScreen(navigateAddDonation: () -> Unit) {

    val navigation = rememberNavigation(defaultRoute = Dashboard)
    val route by navigation.routeStack.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Goodwill Store Suwon", style = MaterialTheme.typography.h3) },
                actions = {
                    IconButton(onClick = { println("click Icon!!!") }) { AppIcon(imageVector = Icons.Default.Settings) }
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
                    navigateDashboard = { navigation.navigate(Dashboard) },
                    navigateDonation = { navigation.navigate(Donation) },
                    navigateSearch = { navigation.navigate(Search) },
                    navigateReport = { navigation.navigate(Report) }
                )
                // content side
                HomeContent(modifier = Modifier.weight(3f)) {
                    when (route) {
                        Dashboard -> EmptyScreen()
                        Donation -> DonationContent(navigateAddDonation = navigateAddDonation)
                        Search -> EmptyScreen()
                        Report -> EmptyScreen()
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