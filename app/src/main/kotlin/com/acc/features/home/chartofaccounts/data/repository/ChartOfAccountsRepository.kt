package com.acc.features.home.chartofaccounts.data.repository

import com.acc.features.home.chartofaccounts.data.local.dao.ChartOfAccountsDao
import com.acc.features.home.chartofaccounts.model.ChartAccount
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChartOfAccountsRepository @Inject constructor(private val dao: ChartOfAccountsDao) {

    suspend fun insertAccount(number: String, description: String, partnerId: String, organizationId: String) {
        dao.insertAccount(number, description, partnerId, organizationId)
    }

    suspend fun deleteAccount(id: String) {
        dao.deleteAccount(id)
    }

    fun getChartOfAccounts(): Flow<List<ChartAccount>> {
        return dao.getChartOfAccounts()
    }
}