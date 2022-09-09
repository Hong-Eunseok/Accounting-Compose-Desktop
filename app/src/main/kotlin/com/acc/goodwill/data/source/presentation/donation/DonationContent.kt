package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import com.acc.common.components.AppIcon

@Composable
fun DonationContent(navigateAddDonation: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateAddDonation) {
                AppIcon(imageVector = Icons.Default.Add)
            }
        }
    ) {

    }
}