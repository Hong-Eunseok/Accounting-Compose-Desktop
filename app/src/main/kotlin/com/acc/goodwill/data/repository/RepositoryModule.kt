package com.acc.goodwill.data.repository

import com.acc.goodwill.domain.repository.DonationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class RepositoryModule {
    @Provides @Singleton fun provideDonationRepository(impl: DonationRepositoryImpl): DonationRepository = impl

}