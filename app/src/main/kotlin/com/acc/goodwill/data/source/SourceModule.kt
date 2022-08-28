package com.acc.goodwill.data.source

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class SourceModule {
    @Provides @Singleton fun providePreferences(impl: PreferencesImpl): Preferences = impl
}