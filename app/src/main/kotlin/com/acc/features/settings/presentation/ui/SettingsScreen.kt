package com.acc.features.settings.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.AppTextField
import com.acc.common.locale.presentation.model.Croatian
import com.acc.common.locale.presentation.model.English
import com.acc.common.locale.presentation.model.LocaleComposition
import com.acc.common.locale.presentation.viewmodel.LocaleViewModel
import com.acc.common.theme.viewmodel.ThemeViewModel
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.smallPadding
import com.acc.di.AppComponent
import com.acc.features.di.ViewModel
import com.acc.features.settings.presentation.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsScreen(appComponent: AppComponent) {
    init {
        appComponent.inject(this)
    }

    @Inject lateinit var settingsViewModel: SettingsViewModel
    @Inject lateinit var themeViewModel: ThemeViewModel
    @Inject lateinit var localeViewModel: LocaleViewModel


    @Composable
    fun SettingsScreen(
        navigateOrganizationSelection: () -> Unit,
        navigateBack: () -> Unit
    ) {

        val locale = LocaleComposition.current

        var vatRate by remember { mutableStateOf<String?>(null) }
        val vatError by settingsViewModel.vatUpdateError.collectAsState()

        val darkTheme by themeViewModel.darkTheme.collectAsState()

        var showLocales by remember { mutableStateOf(false) }
        val availableLocale by remember { mutableStateOf(listOf(English, Croatian)) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = locale.settingsToolbarTitle, style = MaterialTheme.typography.h3) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Card(modifier = Modifier.width(220.dp)) {
                    Column(modifier = Modifier.padding(horizontal = mediumPadding, vertical = smallPadding)) {
                        Button(onClick = {
                            navigateOrganizationSelection()
                            settingsViewModel.unselectOrganizations()
                        }) {
                            Text(text = locale.reselectOrganizationButton)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = locale.settingsDarkThemeButton)
                            Checkbox(
                                checked = darkTheme,
                                onCheckedChange = { themeViewModel.toggleTheme() }
                            )
                        }
                        AppTextField(
                            value = vatRate ?: settingsViewModel.vatRate.toString(),
                            setValue = {
                                vatRate = it
                                settingsViewModel.storeVatRate(it)
                            },
                            label = locale.settingsVatRate,
                            errorMessage = if (vatError) locale.settingsVatRateError else null
                        )
                        Box {
                            OutlinedButton(
                                onClick = { showLocales = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = locale.language)
                                    AppIcon(imageVector = if (showLocales) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown)
                                }
                            }
                            DropdownMenu(
                                expanded = showLocales,
                                onDismissRequest = { showLocales = false }
                            ) {
                                availableLocale.forEach {
                                    DropdownMenuItem(onClick = {
                                        showLocales = false
                                        localeViewModel.updateSelectedLocale(it)
                                    }) {
                                        Text(text = it.language)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
