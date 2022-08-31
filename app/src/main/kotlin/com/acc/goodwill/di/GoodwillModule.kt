package com.acc.goodwill.di

import com.acc.goodwill.data.repository.RepositoryModule
import com.acc.goodwill.data.source.SourceModule
import dagger.Module

@Module(includes = [RepositoryModule::class, SourceModule::class])
class GoodwillModule