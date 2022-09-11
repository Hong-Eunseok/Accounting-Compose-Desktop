package com.acc.goodwill.data.source.presentation.setting

import com.acc.goodwill.data.source.ContributorDao
import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named
import kotlin.streams.toList

class SettingViewModel @Inject constructor(
    @Named("io") private val ioCoroutineScope: CoroutineScope,
    private val contributorDao: ContributorDao,
    private val donationDao: DonationDao
) {

    fun fileOpen(path: String, year: Int) {
        println("fileOpen start $year!!!!")
        ioCoroutineScope.launch {
            val file = File(path)
            val result = withContext(Dispatchers.IO) {
                BufferedReader(InputStreamReader(FileInputStream(file), "utf-8")).use { br ->
                    br.lines().toList()
                }
            }
            result.filter {
                println(it)
                val splits = it.split(",")
                splits.size == 9 &&
                        splits[1].isNotEmpty() &&
                        splits[0].isNotEmpty() &&
                        splits[0].split("/").size == 2
            }.mapIndexedNotNull { index, s ->
                if (index != 0) parseDonate(s, year) else null
            }.toList().forEach { (donate, localDateTime, fromType) ->
                var contributorId = -1L
                if (donate.contributor != null) {
                    contributorId = contributorDao.internalAddContributorAsync(
                        donate.contributor.name,
                        donate.contributor.phoneNumber,
                        donate.contributor.address,
                        donate.contributor.registrationNumber,
                        donate.contributor.registrationType,
                        0,
                        localDateTime
                    )
                }
                donationDao.addDonationOnlyAsync(
                    donate.donate,
                    localDateTime,
                    contributorId,
                    fromType
                )
            }
            println("fileOpen end $year!!!!")
        }
    }

    private fun parseDonate(string: String, year: Int): Triple<Donate, LocalDateTime, Int> {
        val from = listOf("기증", "수거", "기타")
        val now = LocalDateTime.now()
        val splits = string.split(",")
        val donate = Donate(
            contributor = if (splits[1] == "무명") {
                null
            } else {
                Contributor(
                    name = splits[1],
                    phoneNumber = splits[2],
                    address = splits[3],
                    registrationNumber = splits[4],
                    registrationType = 0
                )
            },
            Donate.Donation(
                total = splits[6].toInt(),
                correct = splits[7].toInt(),
                error = splits[6].toInt() - splits[7].toInt(),
                price = splits[8].toULong()
            )
        )
        val fromType = from.indexOf(splits[5])

        val date = splits[0].split("/")
        val registrationDate = now.withYear(year).withHour(0).withMonth(date[0].toInt())
            .withDayOfMonth(date[1].toInt())
        return Triple(donate, registrationDate, fromType)
    }

}