package com.database

import dagger.Provides
import dagger.Module
import javax.inject.Singleton

@Module class DatabaseModule {
    @Provides @Singleton fun provideDatabaseConnection(): DatabaseConnection = DatabaseConnection()
}