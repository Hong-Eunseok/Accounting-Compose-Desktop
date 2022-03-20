package com.acc.features.home.chartofaccounts.add.presentation.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.acc.common.components.AppIcon
import com.acc.common.ui.Strings

@Composable
fun AddChartOfAccountScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = Strings.addAccountToolbarTitle, style = MaterialTheme.typography.h3) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                }
            )
        }
    ) {

    }
}