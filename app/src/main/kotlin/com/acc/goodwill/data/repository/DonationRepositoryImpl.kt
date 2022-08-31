package com.acc.goodwill.data.repository

import com.acc.goodwill.data.source.UserDao
import com.acc.goodwill.domain.repository.DonationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DonationRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : DonationRepository {
    override fun query() {
        println("DonationRepository query")
        userDao.query()
    }
}