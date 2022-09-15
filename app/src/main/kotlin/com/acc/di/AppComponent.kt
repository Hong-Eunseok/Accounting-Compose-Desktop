package com.acc.di

import com.acc.common.di.CommonModule
import com.acc.features.di.FeaturesModule
import com.acc.features.home.chartofaccounts.add.presentation.ui.AddChartOfAccountScreen
import com.acc.features.home.chartofaccounts.list.presentation.ui.ChartOfAccountsScreen
import com.acc.features.home.expenses.add.presentation.ui.AddExpenseScreen
import com.acc.features.home.partners.add.presentation.ui.AddPartnerScreen
import com.acc.features.home.partners.list.presentation.ui.PartnersScreen
import com.acc.features.home.presentation.ui.HomeScreen
import com.acc.features.main.ui.Main
import com.acc.features.organization.create.presentation.ui.CreateOrganizationScreen
import com.acc.features.organization.selection.ui.OrganizationSelectionScreen
import com.acc.features.settings.presentation.ui.SettingsScreen
import com.acc.goodwill.di.GoodwillModule
import com.acc.goodwill.presentation.contributor.ContributorScreen
import com.acc.goodwill.presentation.donation.AddDonationScreen
import com.acc.goodwill.presentation.donation.DetailDonationScreen
import com.acc.goodwill.presentation.donation.SearchContributorContent
import com.acc.goodwill.presentation.report.ReportContent
import com.acc.goodwill.presentation.setting.SettingScreen
import com.database.DatabaseModule
import com.preferences.PreferencesModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DatabaseModule::class, PreferencesModule::class, CommonModule::class, FeaturesModule::class,
        GoodwillModule::class
    ]
)
interface AppComponent {
    fun inject(main: Main)
    fun inject(organizationSelectionScreen: OrganizationSelectionScreen)
    fun inject(homeScreen: AddExpenseScreen)
    fun inject(addChartOfAccountScreen: AddChartOfAccountScreen)
    fun inject(settingsScreen: SettingsScreen)
    fun inject(createOrganizationScreen: CreateOrganizationScreen)
    fun inject(addPartnerScreen: AddPartnerScreen)
    fun inject(chartOfAccountsScreen: ChartOfAccountsScreen)
    fun inject(partnersScreen: PartnersScreen)
    fun inject(main: com.acc.goodwill.presentation.main.Main)
    fun inject(main: ContributorScreen)
    fun inject(main: SearchContributorContent)
    fun inject(main: AddDonationScreen)
    fun inject(main: HomeScreen)
    fun inject(main: DetailDonationScreen)
    fun inject(main: SettingScreen)
    fun inject(main: ReportContent)




}