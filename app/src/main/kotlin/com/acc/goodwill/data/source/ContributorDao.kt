package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.ContributorTable
import com.acc.goodwill.data.source.table.ContributorTable.address
import com.acc.goodwill.data.source.table.ContributorTable.name
import com.acc.goodwill.data.source.table.ContributorTable.phoneNumber
import com.acc.goodwill.data.source.table.ContributorTable.recommend
import com.acc.goodwill.data.source.table.ContributorTable.registrationNumber
import com.acc.goodwill.data.source.table.ContributorTable.registrationType
import com.acc.goodwill.domain.model.Contributor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class ContributorDao @Inject constructor() {

    suspend fun searchContributor(keyword: String): List<Contributor> {
        val data = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.select {
                name like  "%$keyword%" or (phoneNumber like "%$keyword%")
            }.map {
                Contributor(
                    it[name],
                    it[ContributorTable.id],
                    it[phoneNumber],
                    it[address],
                    it[registrationNumber],
                    it[recommend],
                    it[registrationType]
                )
            }
        }
        val result = data.await()
        println("result count ${result.size}")
        return result
    }

    suspend fun addContributor(
        name: String,
        phoneNumber: String?,
        address: String?,
        registrationNumber: String?,
        registrationType: Int,
        join: Int
    ) {
        val id = internalAddContributorAsync(
            name, phoneNumber, address, registrationNumber, registrationType, join
        )

        println("addContributor success $id")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun internalAddContributorAsync(
        name: String,
        phoneNumber: String?,
        address: String?,
        registrationNumber: String?,
        registrationType: Int,
        join: Int,
        createTime: LocalDateTime = LocalDateTime.now(),
        search: Boolean = false
    ): Long {
        if (search) {
            searchContributorByName(name).firstOrNull {
                phoneNumber == it.phoneNumber
            }?.run {
                return primaryKey.value
            }

            if (phoneNumber?.isNotEmpty() == true) {
                searchContributorByPhoneNumber(phoneNumber).firstOrNull()?.run { return primaryKey.value }
            }
        }

        val launch = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.insertAndGetId {
                it[this.name] = name
                it[this.phoneNumber] = phoneNumber.orEmpty()
                it[this.address] = address.orEmpty()
                it[this.registrationNumber] = registrationNumber.orEmpty()
                it[this.registrationType] = registrationType
                it[this.recommend] = join
                it[this.createdAt] = createTime
                it[this.updatedAt] = createTime
                it[this.recentAt] = createTime
            }
        }
        launch.await()
        return launch.getCompleted().value
    }

    private suspend fun searchContributorByName(keyword: String): List<Contributor> {
        val data = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.select {
                name eq keyword
            }.map {
                Contributor(
                    it[name],
                    it[ContributorTable.id],
                    it[phoneNumber],
                    it[address],
                    it[registrationNumber],
                    it[recommend],
                    it[registrationType]
                )
            }
        }
        return data.await()
    }

    private suspend fun searchContributorByPhoneNumber(keyword: String): List<Contributor> {
        val data = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.select {
                phoneNumber eq keyword
            }.map {
                Contributor(
                    it[name],
                    it[ContributorTable.id],
                    it[phoneNumber],
                    it[address],
                    it[registrationNumber],
                    it[recommend],
                    it[registrationType]
                )
            }
        }
        return data.await()
    }

    suspend fun modifyContributor(
        name: String,
        phoneNumber: String?,
        address: String?,
        registrationNumber: String?,
        registrationType: Int,
        join: Int,
        modifyId: Long,
    ) {
        val launchResult = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.update({ ContributorTable.id eq modifyId}) {
                it[this.name] = name
                it[this.phoneNumber] = phoneNumber.orEmpty()
                it[this.address] = address.orEmpty()
                it[this.registrationNumber] = registrationNumber.orEmpty()
                it[this.registrationType] = registrationType
                it[this.recommend] = join
                it[this.updatedAt] = LocalDateTime.now()
            }
        }
        println("Result : ${launchResult.await()}")
    }

    suspend fun updatedRecentTime(contributorId: Long) {
        if (contributorId < 0) return
        val launchResult = suspendedTransactionAsync(Dispatchers.IO) {
            ContributorTable.update({ ContributorTable.id eq contributorId}) {
                it[this.recentAt] = LocalDateTime.now()
            }
        }
        launchResult.await()
    }

}