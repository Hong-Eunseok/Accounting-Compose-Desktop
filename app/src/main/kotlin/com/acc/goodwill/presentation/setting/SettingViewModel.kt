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

    fun fileOpen(path: String) {
        println("fileOpen start !!!!")
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
                    if (split[0].trim().isNotEmpty()) split else null
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
                        parsing.createAt,
                        search = true
                    )
                }
                donationDao.addParsingDonation(contributorId, parsing)
            }
            _result.emit(SnackbarResult.SUCCESS)
        }
        println("fileOpen end !!!!")
    }

    private fun parseDonate(split: List<String>): Parsing {
        val now = LocalDateTime.now()
        val parseDate = LocalDate.parse(split[0], DateTimeFormatter.ISO_DATE)
        println(split.joinToString(","))
        return Parsing(
            createAt = now.withYear(parseDate.year).withMonth(parseDate.monthValue).withDayOfMonth(parseDate.dayOfMonth),
            contributor = when {
                split[1].trim().isEmpty() && split[2].trim().isEmpty() -> null
                split[1].trim().contains("무명") && split[2].trim().isEmpty() -> null
                else -> Parsing.Contributor(
                    name = split[1].trim().ifEmpty { split[2].trim() },
                    phoneNumber = split[2].trim(),
                    address = split[3].trim(),
                    registrationNumber = split[8].trim()
                )
            },
            donation = Parsing.Donation(
                total = split[10].trim().toInt(),
                total_error = split[11].trim().toIntOrNull() ?: 0,
                total_correct = split[12].trim().toIntOrNull() ?: 0,
                price = split[13].trim().toULongOrNull() ?: 0u,
                fromType = Parsing.Donation.FROM_TYPE.indexOf(split[9].trim()),
                optional = split[4].trim(),
                organization = if (split[6].trim() == "기") 1 else 0,
                member = split[7].trim() == "교"
            ),
            products = mutableListOf<Parsing.Product>().apply {
                // 의류
                if (split[14].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 0,
                            total = split[14].trim().toInt(),
                            error = split[15].trim().toIntOrNull() ?: 0,
                            correct = split[16].trim().toIntOrNull() ?: 0,
                        )
                    )
                }

                // 잡화
                if (split[17].trim().isNotEmpty() && split[18].trim().isNotEmpty()) {
                    val total = (split[17].trim().toIntOrNull() ?: 0) + (split[18].trim().toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 2,
                            total = total,
                            error = split[19].trim().toIntOrNull() ?: 0,
                            correct = split[20].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[17].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 2,
                            total = split[17].trim().toIntOrNull() ?: 0,
                            error = split[19].trim().toIntOrNull() ?: 0,
                            correct = split[20].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[18].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 3,
                            total = split[18].trim().toIntOrNull() ?: 0,
                            error = split[19].trim().toIntOrNull() ?: 0,
                            correct = split[20].trim().toIntOrNull() ?: 0,
                        )
                    )
                }

                // 문구완구
                if (split[21].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 4,
                            total = split[21].trim().toInt(),
                            error = split[22].trim().toIntOrNull() ?: 0,
                            correct = split[23].trim().toIntOrNull() ?: 0,
                        )
                    )
                }

                // 도서
                if (split[24].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 1,
                            total = split[24].trim().toInt(),
                            error = split[25].trim().toIntOrNull() ?: 0,
                            correct = split[26].trim().toIntOrNull() ?: 0,
                        )
                    )
                }

                // 가구, 가전
                if (split[27].trim().isNotEmpty() && split[28].trim().isNotEmpty()) {
                    val total = (split[27].trim().toIntOrNull() ?: 0) + (split[28].trim().toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 6,
                            total = total,
                            error = split[29].trim().toIntOrNull() ?: 0,
                            correct = split[30].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[27].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 5,
                            total = split[27].trim().toIntOrNull() ?: 0,
                            error = split[29].trim().toIntOrNull() ?: 0,
                            correct = split[30].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[28].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 6,
                            total = split[28].trim().toIntOrNull() ?: 0,
                            error = split[29].trim().toIntOrNull() ?: 0,
                            correct = split[30].trim().toIntOrNull() ?: 0,
                        )
                    )
                }

                // 주방, 기타
                if (split[31].trim().isNotEmpty() && split[32].trim().isNotEmpty()) {
                    val total = (split[31].trim().toIntOrNull() ?: 0) + (split[32].trim().toIntOrNull() ?: 0)
                    add(
                        Parsing.Product(
                            category = 7,
                            total = total,
                            error = split[33].trim().toIntOrNull() ?: 0,
                            correct = split[34].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[31].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 7,
                            total = split[31].trim().toIntOrNull() ?: 0,
                            error = split[33].trim().toIntOrNull() ?: 0,
                            correct = split[34].trim().toIntOrNull() ?: 0,
                        )
                    )
                } else if (split[32].trim().isNotEmpty()) {
                    add(
                        Parsing.Product(
                            category = 8,
                            total = split[32].trim().toIntOrNull() ?: 0,
                            error = split[33].trim().toIntOrNull() ?: 0,
                            correct = split[34].trim().toIntOrNull() ?: 0,
                        )
                    )
                }
            }
        )
    }

}