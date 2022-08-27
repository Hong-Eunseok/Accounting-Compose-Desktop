package com.acc.common.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module class CommonModule {
    @Provides @Singleton fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}