package com.acc.common.di

import dagger.Module
import dagger.Provides
import jdk.jfr.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module class CommonModule {
    @Named("io") @Provides @Singleton fun provideIoCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
    @Named("main") @Provides @Singleton fun provideMainCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Default)
}