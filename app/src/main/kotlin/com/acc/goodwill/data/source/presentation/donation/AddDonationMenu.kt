package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.common.homeMenuPadding
import com.acc.goodwill.data.source.presentation.home.HomeMenuButton
import com.acc.goodwill.data.source.presentation.navigation.*

@Composable
fun AddDonationMenu(
    currentRoute: DonationRoute,
    screenWidth: Dp,
    navigateSearchContributor: () -> Unit,
    navigateAddProduct: () -> Unit,
    navigateAddConfirm: () -> Unit
) {
    val locale = LocaleComposition.current

    Surface(
        color = Color.LightGray,
        elevation = 3.dp,
        modifier = Modifier.width(minOf((screenWidth / 3), 220.dp)).fillMaxSize().zIndex(5f)
    ) {
        Column(modifier = Modifier.padding(homeMenuPadding)) {
            AddDonationButton(
                text = "1. 검색",
                selected = currentRoute is SearchContribute,
                onClick = navigateSearchContributor
            )
            AddDonationButton(
                text = "2. 기부물품등록",
                selected = currentRoute is AddProduct,
                onClick = navigateAddProduct
            )
            AddDonationButton(
                text = "3. 최종확인",
                selected = currentRoute is Confirm,
                onClick = navigateAddConfirm
            )
        }
    }
}

@Composable
fun AddDonationButton(
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
