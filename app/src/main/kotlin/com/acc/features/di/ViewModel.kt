package com.acc.features.di

import javax.inject.Qualifier

object ViewModel {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AddChartOfAccountsViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ChartOfAccountsViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AddExpenseViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AddPartnerViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PartnersViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class HomeViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OrganizationSelectionViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CreateOrganizationViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SettingsViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocaleViewModel

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ThemeViewModel
}