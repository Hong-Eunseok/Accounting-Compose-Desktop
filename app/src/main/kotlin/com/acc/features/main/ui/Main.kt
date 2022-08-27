package com.acc.features.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.acc.common.locale.presentation.model.LocaleComposition
import com.acc.common.locale.presentation.viewmodel.LocaleViewModel
import com.acc.common.theme.viewmodel.ThemeViewModel
import com.acc.common.ui.AppTheme
import com.acc.di.AppComponent
import com.acc.features.home.chartofaccounts.add.presentation.ui.AddChartOfAccountScreen
import com.acc.features.home.expenses.add.presentation.ui.AddExpenseScreen
import com.acc.features.home.partners.add.presentation.ui.AddPartnerScreen
import com.acc.features.home.presentation.ui.HomeScreen
import com.acc.features.home.sales.add.presentation.ui.AddSalesScreen
import com.acc.features.organization.create.presentation.ui.CreateOrganizationScreen
import com.acc.features.organization.selection.ui.OrganizationSelectionScreen
import com.acc.features.settings.presentation.ui.SettingsScreen
import com.acc.navigation.*
import com.navigation.rememberNavigation
import javax.inject.Inject

class Main(appComponent: AppComponent) {


    @Inject
    lateinit var themeViewModel: ThemeViewModel
    @Inject
    lateinit var localeViewModel: LocaleViewModel


    private val organizationSelectionScreen: OrganizationSelectionScreen by lazy {
        OrganizationSelectionScreen(appComponent)
    }

    private val homeScreen: HomeScreen by lazy {
        HomeScreen(appComponent)
    }

    private val addExpenseScreen: AddExpenseScreen by lazy {
        AddExpenseScreen(appComponent)
    }

    private val addChartOfAccountScreen: AddChartOfAccountScreen by lazy {
        AddChartOfAccountScreen(appComponent)
    }

    private val settingsScreen: SettingsScreen by lazy {
        SettingsScreen(appComponent)
    }

    private val createOrganizationScreen: CreateOrganizationScreen by lazy {
        CreateOrganizationScreen(appComponent)
    }

    private val addPartnerScreen: AddPartnerScreen by lazy {
        AddPartnerScreen(appComponent)
    }

    init {
        appComponent.inject(this)
    }

    @Composable
    fun Main() {
        val stateHolder = rememberSaveableStateHolder()
        val navigation = rememberNavigation(defaultRoute = OrganizationSelectionRoute)
        val route by navigation.routeStack.collectAsState()
        val darkTheme by themeViewModel.darkTheme.collectAsState()
        val selectedLocale by localeViewModel.selectedLocale.collectAsState()

        AppTheme(useDarkTheme = darkTheme) {
            CompositionLocalProvider(LocaleComposition provides selectedLocale) {
                when (route) {
                    is OrganizationSelectionRoute -> {
                        stateHolder.removeState(Unit)
                        organizationSelectionScreen.OrganizationScreen(
                            navigateCreateOrganization = { navigation.navigate(CreateOrganizationRoute) },
                            navigateHome = {
                                navigation.popLast()
                                navigation.navigate(HomeRoute)
                            }
                        )
                    }
                    is HomeRoute -> {
                        stateHolder.SaveableStateProvider(Unit) {
                            homeScreen.HomeScreen(
                                navigateAddExpense = { navigation.navigate(AddExpenseRoute) },
                                navigateAddSales = { navigation.navigate(AddSalesRoute) },
                                navigateAddAccount = { navigation.navigate(AddChartOfAccountRoute) },
                                navigateAddPartner = { navigation.navigate(AddPartnerRoute) },
                                navigateSettings = { navigation.navigate(SettingsRoute) }
                            )
                        }
                    }
                    is AddExpenseRoute -> addExpenseScreen.AddExpenseScreen(navigateBack = navigation::popLast)
                    is AddSalesRoute -> AddSalesScreen(navigateBack = navigation::popLast)
                    is AddChartOfAccountRoute -> addChartOfAccountScreen.AddChartOfAccountScreen(navigateBack = navigation::popLast)
                    is AddPartnerRoute -> addPartnerScreen.AddPartnerScreen(navigateBack = navigation::popLast)
                    is CreateOrganizationRoute -> createOrganizationScreen.CreateOrganizationScreen {
                        navigation.popLast()
                    }
                    is SettingsRoute -> settingsScreen.SettingsScreen(
                        navigateOrganizationSelection = { navigation.navigateAsRoot(OrganizationSelectionRoute) },
                        navigateBack = navigation::popLast
                    )
                }
            }
        }
    }

}


