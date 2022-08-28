package com.acc.goodwill.presentation.common

import com.acc.goodwill.data.source.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class SettingViewModel @Inject constructor(
    private val preferences: Preferences,
    private val ioCoroutineScope: CoroutineScope
) {

    val darkTheme = preferences.getFlow(DARK_THEME_KEY, false)
        .stateIn(ioCoroutineScope, SharingStarted.Lazily, false)

    fun toggleTheme() {
        ioCoroutineScope.launch { preferences.set(DARK_THEME_KEY, !darkTheme.first()) }
    }

    val selectedLocal = preferences.getFlow(LOCALE_KEY, Korean::class.java.name)
        .map { if (it == Korean::class.java.name) Korean else English }
        .stateIn(ioCoroutineScope, SharingStarted.Lazily, Korean)

    fun updateSelectedLocal(local: Locale) {
        ioCoroutineScope.launch { preferences.set(LOCALE_KEY, local::class.java.name) }
    }

    companion object {
        const val DARK_THEME_KEY = "dark_theme_key"
        const val LOCALE_KEY = "locale_key"
    }

}