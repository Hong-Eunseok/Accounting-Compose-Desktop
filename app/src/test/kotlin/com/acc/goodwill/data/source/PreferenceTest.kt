package com.acc.goodwill.data.source

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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


}