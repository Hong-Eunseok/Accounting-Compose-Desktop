package com.acc.goodwill.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acc.goodwill.presentation.common.LocaleComposition
import com.acc.goodwill.presentation.common.homeMenuPadding
import com.acc.goodwill.presentation.navigation.HomeReport
import com.acc.goodwill.presentation.navigation.HomeSearch
import com.acc.goodwill.presentation.navigation.HomeStatistics
import com.navigation.Route
import com.acc.goodwill.presentation.navigation.HomeDonation as HomeDonation1


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
                selected = currentRoute is HomeDonation1,
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