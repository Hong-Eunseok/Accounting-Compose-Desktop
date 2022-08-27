package com.preferences

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class PreferencesModule {
    @Provides @Singleton fun providePreferences(): Preferences = Preferences()
}

