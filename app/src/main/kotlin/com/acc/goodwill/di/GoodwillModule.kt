package com.acc.goodwill.di

import com.acc.goodwill.data.source.SourceModule
import dagger.Module

@Module(includes = [SourceModule::class])
class GoodwillModule