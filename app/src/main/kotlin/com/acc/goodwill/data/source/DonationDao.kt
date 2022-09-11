package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.DonationTable
import com.acc.goodwill.data.source.table.ProductTable
import com.acc.goodwill.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DonationDao @Inject constructor() {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun addDonation(
        contributorId: Long,
        products: List<Product>,
        confirmInfo: Triple<Int, Int, Boolean>
    ): Throwable? {
        val launchResult = suspendedTransactionAsync(Dispatchers.IO) {
            val createTime = LocalDateTime.now()
            val donationEntityId = DonationTable.insertAndGetId { table ->
                table[this.contributorId] = contributorId
                table[this.total] = products.sumOf { it.total }.toInt()
                table[this.total_error] = products.sumOf { it.error }.toInt()
                table[this.total_correct] = products.sumOf { it.correct }.toInt()
                table[this.price] = products.sumOf { it.transferPrice }
                table[this.fromType] = confirmInfo.first
                table[this.organization] = confirmInfo.second
                table[this.member] = confirmInfo.third
                table[this.createdAt] = createTime
            }

            val donationId = donationEntityId.value

            products.forEach { product ->
                ProductTable.insert { table ->
                    table[this.contributorId] = contributorId
                    table[this.donationId] = donationId
                    table[this.category] = product.category
                    table[this.label] = product.label
                    table[this.total] = product.total.toInt()
                    table[this.error] = product.error.toInt()
                    table[this.correct] = product.correct.toInt()
                    table[this.price] = product.price
                    table[this.transferPrice] = product.transferPrice
                    table[this.createdAt] = createTime
                }
            }
        }
        println("addDonation await : ${launchResult.await()}")
        return launchResult.getCompletionExceptionOrNull()
    }

}