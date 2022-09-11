package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.ContributorTable
import com.acc.goodwill.data.source.table.ContributorTable.address
import com.acc.goodwill.data.source.table.ContributorTable.name
import com.acc.goodwill.data.source.table.ContributorTable.phoneNumber
import com.acc.goodwill.data.source.table.ContributorTable.recommend
import com.acc.goodwill.data.source.table.ContributorTable.registrationNumber
import com.acc.goodwill.domain.model.Contributor
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.EntityID
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
        val createTime = LocalDateTime.now()
        var id: EntityID<Long>? = null
        val launchResult = suspendedTransactionAsync(Dispatchers.IO) {
            id = ContributorTable.insertAndGetId {
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
        println("Result : ${launchResult.await()}")
        println("addUser id : ${id?.value ?: -1}")
    }

}