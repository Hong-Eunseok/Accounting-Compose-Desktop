package com.acc.goodwill.presentation.setting

import com.acc.goodwill.data.source.ContributorDao
import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named
import kotlin.streams.toList

class SettingViewModel @Inject constructor(
    @Named("io") private val ioCoroutineScope: CoroutineScope,
    private val contributorDao: ContributorDao,
    private val donationDao: DonationDao
) {

    private val _result: MutableStateFlow<SnackbarResult> = MutableStateFlow(SnackbarResult.IDLE)
    val result: StateFlow<SnackbarResult> = _result

    fun fileOpen(path: String, year: Int) {
        println("fileOpen start $year!!!!")
        ioCoroutineScope.launch {
            val file = File(path)
            val result = withContext(Dispatchers.IO) {
                BufferedReader(InputStreamReader(FileInputStream(file), "utf-8")).use { br ->
                    br.lines().toList()
                }
            }
            result.mapIndexedNotNull { index, s ->
                if (index == 0) {
                    null
                } else {
                    val split = s.split(",")
                    if (split[0].isNotEmpty()) split else null
                }
            }.map { split ->
                parseDonate(split)
            }.forEach { parsing ->
                var contributorId = -1L
                if (parsing.contributor != null) {
                    contributorId = contributorDao.internalAddContributorAsync(
                        parsing.contributor.name,
                        parsing.contributor.phoneNumber,
                        parsing.contributor.address,
                        parsing.contributor.registrationNumber,
                        parsing.contributor.registrationType,
                        0,
                        parsing.createAt
                    )
                }
                donationDao.addParsingDonation(contributorId, parsing)
            }
            _result.emit(SnackbarResult.SUCCESS)
        }
        println("fileOpen end $year!!!!")
    }

    private fun parseDonate(split: List<String>): Parsing {
        val now = LocalDateTime.now()
        val parseDate = LocalDate.parse(split[0], DateTimeFormatter.ISO_DATE)
        return Parsing(
            createAt = now.withYear(parseDate.year).withMonth(parseDate.monthValue).withDayOfYear(parseDate.dayOfYear),
            contributor = if (split[1].isEmpty()) {
                null
            } else {
                Parsing.Contributor(
                    name = split[1],
                    phoneNumber = split[2],
                    address = split[3],
                    registrationNumber = split[6]
                )
            },
            donation = Parsing.Donation(
                total = split[8].toInt(),
                total_error = split[9].toIntOrNull() ?: 0,
                total_correct = split[10].toIntOrNull() ?: 0,
                price = split[11].toULongOrNull() ?: 0u,
                fromType = Parsing.Donation.FROM_TYPE.indexOf(split[7]),
                optional = split[4]
            ),
            products = mutableListOf<Parsing.Product>().apply {
                // 의류
                if (split[12].isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 0,
                            total = split[12].toInt(),
                            error = split[13].toIntOrNull() ?: 0,
                            correct = split[14].toIntOrNull() ?: 0,
                        )
                    )
                }

                // 잡화
                if (split[15].isNotEmpty() || split[16].isNotEmpty()) {
                    val total = (split[15].toIntOrNull() ?: 0) + (split[16].toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 2,
                            total = total,
                            error = split[17].toIntOrNull() ?: 0,
                            correct = split[18].toIntOrNull() ?: 0,
                        )
                    )
                }

                // 문구완구
                if (split[19].isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 4,
                            total = split[19].toInt(),
                            error = split[20].toIntOrNull() ?: 0,
                            correct = split[21].toIntOrNull() ?: 0,
                        )
                    )
                }

                // 도서
                if (split[22].isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 1,
                            total = split[22].toInt(),
                            error = split[23].toIntOrNull() ?: 0,
                            correct = split[24].toIntOrNull() ?: 0,
                        )
                    )
                }

                // 가구, 가전
                if (split[25].isNotEmpty() || split[26].isNotEmpty()) {
                    val total = (split[25].toIntOrNull() ?: 0) + (split[26].toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 6,
                            total = total,
                            error = split[27].toIntOrNull() ?: 0,
                            correct = split[28].toIntOrNull() ?: 0,
                        )
                    )
                }

                // 주방, 기타
                if (split[29].isNotEmpty() || split[30].isNotEmpty()) {
                    val total = (split[29].toIntOrNull() ?: 0) + (split[30].toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 6,
                            total = total,
                            error = split[31].toIntOrNull() ?: 0,
                            correct = split[32].toIntOrNull() ?: 0,
                        )
                    )
                }
            }
        )
    }

}