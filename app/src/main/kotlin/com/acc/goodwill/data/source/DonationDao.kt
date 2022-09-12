package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.ContributorTable
import com.acc.goodwill.data.source.table.DonationTable
import com.acc.goodwill.data.source.table.ProductTable
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Product
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.Parsing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
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

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun addParsingDonation(contributorId: Long, parsing: Parsing): Throwable? {
        val launchResult = suspendedTransactionAsync(Dispatchers.IO) {
            val donationEntityId = DonationTable.insertAndGetId { table ->
                table[this.contributorId] = contributorId
                table[this.total] = parsing.donation.total
                table[this.total_error] = parsing.donation.total_error
                table[this.total_correct] = parsing.donation.total_correct
                table[this.price] = parsing.donation.price
                table[this.fromType] = parsing.donation.fromType
                table[this.organization] = parsing.donation.organization
                table[this.member] = parsing.donation.member
                table[this.optionalParsingData] = parsing.donation.optional
                table[this.createdAt] = parsing.createAt
            }

            val donationId = donationEntityId.value

            parsing.products.forEach { product ->
                ProductTable.insert { table ->
                    table[this.contributorId] = contributorId
                    table[this.donationId] = donationId
                    table[this.category] = product.category
                    table[this.label] = product.label
                    table[this.total] = product.total
                    table[this.error] = product.error
                    table[this.correct] = product.correct
                    table[this.price] = product.price
                    table[this.transferPrice] = product.transferPrice
                    table[this.createdAt] = parsing.createAt
                }
            }
        }
        launchResult.await()
        return launchResult.getCompletionExceptionOrNull()
    }

    suspend fun queryTodayDonation(): List<Donate> {
        val now = LocalDateTime.now()
        val before = now.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val after = before.plusDays(1)

        println("queryTodayDonation before $before")
        println("queryTodayDonation after $after")

        val data = suspendedTransactionAsync(Dispatchers.IO) {
            DonationTable.join(ContributorTable, JoinType.LEFT, DonationTable.contributorId, ContributorTable.id)
                .select {
                    DonationTable.createdAt.between(before, after)
                }
                .map {
                    Donate(
                        contributor = if (it[DonationTable.contributorId] == -1L) {
                            null
                        } else {
                            Contributor(
                                name = it[ContributorTable.name],
                                primaryKey = it[ContributorTable.id],
                                phoneNumber = it[ContributorTable.phoneNumber],
                                address = it[ContributorTable.address],
                                registrationNumber = it[ContributorTable.registrationNumber],
                                recommend = it[ContributorTable.recommend]
                            )
                        },
                        donate = Donate.Donation(
                            total = it[DonationTable.total],
                            error = it[DonationTable.total_error],
                            correct = it[DonationTable.total_correct],
                            price = it[DonationTable.price],
                        ),
                        primaryKey = it[DonationTable.id]
                    )
                }
        }

        val result = data.await()
        println("queryTodayDonation result count ${result.size}")
        return result
    }

    suspend fun queryProducts(donationId: Long): List<Product> {
        val data = suspendedTransactionAsync(Dispatchers.IO) {
            ProductTable.select {
                ProductTable.donationId eq donationId
            }.map {
                Product(
                    category = it[ProductTable.category],
                    label = it[ProductTable.label],
                    total = it[ProductTable.total].toUInt(),
                    error = it[ProductTable.error].toUInt(),
                    price = it[ProductTable.price],
                    correct = it[ProductTable.correct].toUInt(),
                    transferPrice = it[ProductTable.transferPrice]
                )
            }
        }
        val result = data.await()
        println("queryTodayDonation result count ${result.size}")
        return result
    }

}