package com.acc.goodwill.data.source

import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PreferenceTest {

    private lateinit var source: Preferences

    @BeforeEach fun setUp() {
        source = PreferencesImpl()
    }

    @AfterEach fun tearDown() {
        source.clear()
    }

    @Test fun setBooleanAndGet() {
        assertThat(source.get("test", false)).isFalse()
        assertThat(source.get("test", true)).isTrue()
        source.set("test", true)
        assertThat(source.get("test", false)).isTrue()
    }

    @Test fun setStringAndGet() {
        assertThat(source.get("test", "test")).isEqualTo("test")
        assertThat(source.get("test", "")).isEmpty()
        source.set("test", "abc")
        assertThat(source.get("test", "test")).isEqualTo("abc")
    }

    @Test fun setFloatAndGet() {
        assertThat(source.get("test", 1f)).isEqualTo(1f)
        source.set("test", 2f)
        assertThat(source.get("test", 1f)).isEqualTo(2f)
    }

    @Test fun setLongAndGet() {
        assertThat(source.get("test", 1L)).isEqualTo(1L)
        source.set("test", 2L)
        assertThat(source.get("test", 1L)).isEqualTo(2L)
    }

    @Test fun setIntAndGet() {
        assertThat(source.get("test", 1.toInt())).isEqualTo(1)
        source.set("test", 2.toInt())
        assertThat(source.get("test", 1.toInt())).isEqualTo(2)
    }

    @Test
    fun test() {
        val now = LocalDateTime.now()
        val before = now.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val after = before.plusDays(1)
        println("now : $now")
        println("before : $before")
        println("before : $after")
    }

    // 날짜,이름,번호,주소,주민등록,기증,전체숫자,양품수,환산금액
    @Test
    fun parseTest() {
        val from = listOf("기증", "수거", "기타")
        val organization = listOf("개인", "단체")

        val correct = "8/1,김윤혁,000-0000-1111,서울시 용산구 신계동 48    용산e편한세상 @108-1502,221015-1361417,기증,322,317,228000"
        val error = "8/1,,000-0000-1115,수원시 영통구 동수원로 316 임광@ 8동 701호 ,221015-1361421,수거,35,12,41000"
        val now = LocalDateTime.now()

        listOf(correct, error).filter {
            val splits = it.split(",")
            splits.size == 9 &&splits[1].isNotEmpty()
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
            val registrationDate = now.withYear(2021).withHour(0).withMonth(date[0].toInt()).withDayOfMonth(date[1].toInt())
            Triple(donate, registrationDate, fromType)
        }.forEach {
            println("$it")
        }
    }


}
