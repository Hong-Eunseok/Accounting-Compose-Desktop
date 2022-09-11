package com.acc.features.home.presentation.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.di.AppComponent
import com.acc.features.di.ViewModel
import com.acc.features.home.chartofaccounts.list.presentation.ui.ChartOfAccountsScreen
import com.acc.features.home.dashboard.DashboardScreen
import com.acc.features.home.expenses.list.presentation.ui.ExpensesScreen
import com.acc.features.home.navigation.*
import com.acc.features.home.partners.list.presentation.ui.PartnersScreen
import com.acc.features.home.presentation.viewmodel.HomeViewModel
import com.acc.features.home.sales.list.presentation.ui.SalesScreen
import com.navigation.rememberNavigation
import javax.inject.Inject

class HomeScreen(appComponent: AppComponent) {

    @Inject
    lateinit var viewModel: HomeViewModel

    private val chartOfAccountsScreen: ChartOfAccountsScreen by lazy {
        ChartOfAccountsScreen(appComponent)
    }

    private val partnersScreen: PartnersScreen by lazy {
        PartnersScreen(appComponent)
    }

    init {
//        appComponent.inject(this)
    }

    @Composable
    fun HomeScreen(
        navigateAddExpense: () -> Unit,
        navigateAddSales: () -> Unit,
        navigateAddAccount: () -> Unit,
        navigateAddPartner: () -> Unit,
        navigateSettings: () -> Unit
    ) {

        val homeNavigation = rememberNavigation(defaultRoute = Dashboard)
        val route by homeNavigation.routeStack.collectAsState()

        val toolbarTitle by viewModel.selectedOrganizationName.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = toolbarTitle, style = MaterialTheme.typography.h3) },
                    actions = {
                        IconButton(onClick = navigateSettings) { AppIcon(imageVector = Icons.Default.Settings) }
                    }
                )
            }
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val screenWidth = this.constraints.maxWidth
                Row(modifier = Modifier.fillMaxWidth()) {
                    HomeMenu(
                        currentRoute = route,
                        navigateDashboard = { homeNavigation.navigate(Dashboard) },
                        navigateExpenses = { homeNavigation.navigate(Expenses) },
                        navigateSales = { homeNavigation.navigate(Sales) },
                        navigateCharOfAccounts = { homeNavigation.navigate(CharOfAccounts) },
                        navigatePartners = { homeNavigation.navigate(Partners) },
                        modifier = Modifier.menuWidth(screenWidth.dp)
                    )
                    HomeContent(modifier = Modifier.weight(3f)) {
                        when (route) {
                            is Dashboard -> DashboardScreen()
                            is Expenses -> ExpensesScreen(navigateAddExpense = navigateAddExpense)
                            is Sales -> SalesScreen(navigateAddSales = navigateAddSales)
                            is CharOfAccounts -> chartOfAccountsScreen.ChartOfAccountsScreen(navigateAddAccount = navigateAddAccount)
                            is Partners -> partnersScreen.PartnersScreen(navigateAddPartner = navigateAddPartner)
                        }
                    }
                }
            }
        }
    }

    @Stable
    private fun Modifier.menuWidth(screenWidth: Dp): Modifier {
        val width = (screenWidth / 3)
        val maxWidth = 220.dp
        return this.width(minOf(width, maxWidth))
    }
}
