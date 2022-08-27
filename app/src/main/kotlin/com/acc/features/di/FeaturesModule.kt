package com.acc.features.di

import com.acc.common.locale.presentation.viewmodel.LocaleViewModel
import com.acc.common.theme.viewmodel.ThemeViewModel
import com.acc.features.home.chartofaccounts.add.presentation.viewmodel.AddChartOfAccountsViewModel
import com.acc.features.home.chartofaccounts.data.local.dao.ChartOfAccountsDao
import com.acc.features.home.chartofaccounts.data.local.dao.ChartOfAccountsDaoImpl
import com.acc.features.home.chartofaccounts.data.repository.ChartOfAccountsRepository
import com.acc.features.home.chartofaccounts.list.presentation.viewmodel.ChartOfAccountsViewModel
import com.acc.features.home.expenses.add.presentation.viewmodel.AddExpenseViewModel
import com.acc.features.home.partners.add.presentation.viewmodel.AddPartnerViewModel
import com.acc.features.home.partners.data.local.dao.PartnersDao
import com.acc.features.home.partners.data.local.dao.PartnersDaoImpl
import com.acc.features.home.partners.data.repository.PartnersRepository
import com.acc.features.home.partners.list.presentation.viewmodel.PartnersViewModel
import com.acc.features.home.presentation.viewmodel.HomeViewModel
import com.acc.features.organization.create.presentation.viewmodel.CreateOrganizationViewModel
import com.acc.features.organization.data.local.dao.OrganizationDao
import com.acc.features.organization.data.local.dao.OrganizationDaoImpl
import com.acc.features.organization.data.repository.OrganizationRepository
import com.acc.features.organization.data.repository.OrganizationRepositoryImpl
import com.acc.features.organization.selection.viewmodel.OrganizationSelectionViewModel
import com.acc.features.settings.presentation.viewmodel.SettingsViewModel
import com.navigation.Entry
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module class FeaturesModule {
//    @Provides @Singleton fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

//    @Provides @Singleton @ViewModel.AddChartOfAccountsViewModel fun provideAddChartOfAccountsViewModel(viewModel: AddChartOfAccountsViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.ChartOfAccountsViewModel fun provideChartOfAccountsViewModel(viewModel: ChartOfAccountsViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.AddExpenseViewModel fun provideAddExpenseViewModel(viewModel: AddExpenseViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.AddPartnerViewModel fun provideAddPartnerViewModel(viewModel: AddPartnerViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.PartnersViewModel fun providePartnersViewModel(viewModel: PartnersViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.HomeViewModel fun provideHomeViewModel(viewModel: HomeViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.OrganizationSelectionViewModel fun provideOrganizationSelectionViewModel(viewModel: OrganizationSelectionViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.CreateOrganizationViewModel fun provideCreateOrganizationViewModel(viewModel: CreateOrganizationViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.SettingsViewModel fun provideSettingsViewModel(viewModel: SettingsViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.LocaleViewModel fun provideLocalViewModel(viewModel: LocaleViewModel): Entry = viewModel
//    @Provides @Singleton @ViewModel.ThemeViewModel fun provideThemeViewModel(viewModel: ThemeViewModel): Entry = viewModel

    @Provides @Singleton fun provideChartOfAccountsDao(impl: ChartOfAccountsDaoImpl): ChartOfAccountsDao = impl
    @Provides fun provideChartOfAccountsRepository(dao: ChartOfAccountsDao): ChartOfAccountsRepository = ChartOfAccountsRepository(dao)

    @Provides @Singleton fun providePartnersDao(impl: PartnersDaoImpl): PartnersDao = impl
    @Provides fun providePartnersRepository(dao: PartnersDao): PartnersRepository = PartnersRepository(dao)

    @Provides @Singleton fun provideOrganizationDao(impl: OrganizationDaoImpl): OrganizationDao = impl
    @Provides fun provideOrganizationRepository(dao: OrganizationDao): OrganizationRepository = OrganizationRepositoryImpl(dao)

}