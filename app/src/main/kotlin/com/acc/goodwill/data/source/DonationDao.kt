package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.ContributorTable
import com.acc.goodwill.data.source.table.DonationTable
import com.acc.goodwill.data.source.table.ProductTable
import com.acc.goodwill.domain.model.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.time.LocalDateTime
import java.util.Calendar
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

        val data = queryDonationBetweenAsync(before, after)
        val result = data.await()
        println("queryTodayDonation result count ${result.size}")
        return result
    }

    suspend fun queryProducts(donationId: Long): List<Product> {
        val data = queryProductsAsync(donationId)
        val result = data.await()
        println("queryTodayDonation result count ${result.size}")
        return result
    }

    suspend fun queryMonths(year: Int): List<MonthlyStatics> {
        val results = mutableListOf<MonthlyStatics>()
        for (i in 1..12) {
            val (before, after) = betweenDate(year, i)
            println("between date $before ~ $after")
            results.add(
                MonthlyStatics(
                    i,
                    queryDonationBetweenAsync(before, after).await().map {
                        DonationStatics(it, queryProductsAsync(it.donateId.value).await())
                    }
                )
            )
        }
        return results
    }

    suspend fun queryDonationByContributeIdAsync(contributorId: Long): ContributorSearchResult {
        return ContributorSearchResult(
            queryDonationByContributorIdAsync(contributorId).await().map {
                DonationStatics(it, queryProductsAsync(it.donateId.value).await())
            }
        )
    }

    private fun betweenDate(
        year: Int,
        month: Int,
        now: LocalDateTime = LocalDateTime.now()
    ): Pair<LocalDateTime, LocalDateTime> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        val endDay: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        println("month : $month endDay : $endDay")
        return now.withYear(year).withMonth(month).withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0) to
                now.withYear(year).withMonth(month).withDayOfMonth(endDay)
                    .withHour(23).withMinute(59).withSecond(0).withNano(0)
    }

    private suspend fun queryProductsAsync(donationId: Long): Deferred<List<Product>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
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
    }

    private suspend fun queryDonationBetweenAsync(
        before: LocalDateTime,
        after: LocalDateTime
    ): Deferred<List<Donate>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            DonationTable
                .join(ContributorTable, JoinType.LEFT, DonationTable.contributorId, ContributorTable.id)
                .select {
                    DonationTable.createdAt.between(before, after)
                }
                .orderBy(DonationTable.createdAt)
                .map {
                    Donate(
                        donateId = it[DonationTable.id],
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
                            createAt = it[DonationTable.createdAt],
                            optionParsingData = it[DonationTable.optionalParsingData],
                            organization = it[DonationTable.organization],
                            member = it[DonationTable.member],
                            fromType = it[DonationTable.fromType]
                        ),
                        primaryKey = it[DonationTable.id]
                    )
                }
        }
    }

    private suspend fun queryDonationByContributorIdAsync(
        contributorId: Long
    ): Deferred<List<Donate>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            DonationTable
                .join(ContributorTable, JoinType.LEFT, DonationTable.contributorId, ContributorTable.id)
                .select {
                    DonationTable.contributorId eq contributorId
                }
                .orderBy(DonationTable.createdAt, SortOrder.DESC)
                .map {
                    Donate(
                        donateId = it[DonationTable.id],
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
                            createAt = it[DonationTable.createdAt],
                            optionParsingData = it[DonationTable.optionalParsingData],
                            organization = it[DonationTable.organization],
                            member = it[DonationTable.member],
                            fromType = it[DonationTable.fromType]
                        ),
                        primaryKey = it[DonationTable.id]
                    )
                }
        }
    }

}