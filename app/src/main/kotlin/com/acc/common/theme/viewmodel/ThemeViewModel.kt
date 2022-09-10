package com.acc.common.theme.viewmodel

import com.navigation.Entry
import com.preferences.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ThemeViewModel @Inject constructor(
    private val preferences: Preferences,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) : Entry {

    val darkTheme = preferences.getFlow(DARK_THEME_KEY, false)
        .stateIn(
            ioCoroutineScope,
            SharingStarted.Lazily,
            false
        )

    fun toggleTheme() {
        ioCoroutineScope.launch {
            preferences.set(DARK_THEME_KEY, !darkTheme.first())
        }
    }

    companion object {
        const val DARK_THEME_KEY = "darkThemeKey"
    }
}