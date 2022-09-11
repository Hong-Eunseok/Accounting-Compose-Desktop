package com.acc.goodwill.data.source.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acc.goodwill.data.source.presentation.navigation.HomeDonation
import com.acc.goodwill.data.source.presentation.navigation.HomeReport
import com.acc.goodwill.data.source.presentation.navigation.HomeSearch
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.common.homeMenuPadding
import com.acc.goodwill.data.source.presentation.navigation.HomeStatistics
import com.navigation.Route


@Composable
fun HomeMenu(
    currentRoute: Route,
    screenWidth: Dp,
    navigateStatistics: () -> Unit,
    navigateDonation: () -> Unit,
    navigateSearch: () -> Unit,
    navigateReport: () -> Unit
) {

    val locale = LocaleComposition.current

    Surface(
        color = Color.LightGray,
        elevation = 3.dp,
        modifier = Modifier.width(minOf((screenWidth / 3), 220.dp)).fillMaxSize().zIndex(5f)
    ) {
        Column(modifier = Modifier.padding(homeMenuPadding)) {
            HomeMenuButton(
                text = locale.donation,
                selected = currentRoute is HomeDonation,
                onClick = navigateDonation
            )
            HomeMenuButton(
                text = locale.search,
                selected = currentRoute is HomeSearch,
                onClick = navigateSearch
            )
            HomeMenuButton(
                text = locale.statistics,
                selected = currentRoute is HomeStatistics,
                onClick = navigateStatistics
            )
            HomeMenuButton(
                text = locale.report,
                selected = currentRoute is HomeReport,
                onClick = navigateReport
            )
        }
    }
}

@Composable
fun HomeMenuButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
        ),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)
    }
}