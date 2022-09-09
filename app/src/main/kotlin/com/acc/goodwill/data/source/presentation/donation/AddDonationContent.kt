package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AddDonationContent(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxHeight().background(Color.Gray)) {
        content()
    }
}