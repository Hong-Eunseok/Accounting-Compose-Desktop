package com.acc.goodwill.data.source

import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.Parsing
import com.acc.goodwill.domain.model.Product
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.streams.toList

class ParseTest {


    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun test() {
        val scope = CoroutineScope(Dispatchers.IO)
        val file = File("C:\\Users\\hong\\Desktop\\goodwill\\fullversion.csv")
        val launcher = scope.launch {
            val result = withContext(Dispatchers.IO) {
                BufferedReader(InputStreamReader(FileInputStream(file), "utf-8")).use { br ->
                    br.lines().toList()
                }
            }

            val now = LocalDateTime.now()
            result.mapIndexedNotNull { index, s ->
                if (index == 0) {
                    null
                } else {
                    val split = s.split(",")
                    if (split[0].isNotEmpty()) split else null
                }
            }.map { split ->
                val parseDate = LocalDate.parse(split[0], DateTimeFormatter.ISO_DATE)
                Parsing(
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
            }.forEach {
                println(it)
            }
        }

        while (true) {
            if (launcher.isCompleted) {
                return
            } else {
                sleep(1000)
            }
        }

    }

    // 날짜,이름,번호,주소,주민등록,기증,전체숫자,양품수,환산금액
    @Test
    fun parseTest() {
        val from = listOf("기증", "수거", "기타")
        val organization = listOf("개인", "단체")

        val correct =
            "8/1,김윤혁,000-0000-1111,서울시 용산구 신계동 48    용산e편한세상 @108-1502,221015-1361417,기증,322,317,228000"
        val error = "8/1,,000-0000-1115,수원시 영통구 동수원로 316 임광@ 8동 701호 ,221015-1361421,수거,35,12,41000"
        val now = LocalDateTime.now()

        listOf(correct, error).filter {
            val splits = it.split(",")
            splits.size == 9 && splits[1].isNotEmpty()
        }.map {
            val splits = it.split(",")
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
            val registrationDate = now.withYear(2021).withHour(0).withMonth(date[0].toInt())
                .withDayOfMonth(date[1].toInt())
            Triple(donate, registrationDate, fromType)
        }.forEach {
            println("$it")
        }
    }


}
