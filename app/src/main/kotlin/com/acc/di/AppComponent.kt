package com.acc.di

import com.acc.common.di.CommonModule
import com.acc.features.di.FeaturesModule
import com.acc.goodwill.di.GoodwillModule
import com.acc.goodwill.presentation.contributor.ContributorScreen
import com.acc.goodwill.presentation.donation.AddDonationScreen
import com.acc.goodwill.presentation.contributor.DetailContributorScreen
import com.acc.goodwill.presentation.donation.SearchContributorContent
import com.acc.goodwill.presentation.home.HomeScreen
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
    fun inject(main: com.acc.goodwill.presentation.main.Main)
    fun inject(main: ContributorScreen)
    fun inject(main: SearchContributorContent)
    fun inject(main: AddDonationScreen)
    fun inject(main: DetailContributorScreen)
    fun inject(main: SettingScreen)
    fun inject(main: ReportContent)
    fun inject(main: HomeScreen)

}