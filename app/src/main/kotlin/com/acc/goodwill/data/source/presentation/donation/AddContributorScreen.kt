package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.goodwill.data.source.presentation.navigation.Confirm
import com.acc.goodwill.data.source.presentation.navigation.DonationRoute
import com.acc.goodwill.data.source.presentation.navigation.SearchContribute
import com.navigation.rememberNavigation

@Composable
fun AddContributorScreen(navigateBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "기부자 정보 입력", style = MaterialTheme.typography.h4) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                }
            )
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            Text("success")
//            val screenWidth = constraints.maxWidth
//            Row(Modifier.fillMaxWidth()) {
//                // menu side
//                AddDonationMenu(
//                    route as DonationRoute,
//                    screenWidth.dp,
//                    navigateSearchContributor = { navigation.navigate(SearchContribute) },
//                    navigateAddProduct = { navigation.navigate(com.acc.goodwill.data.source.presentation.navigation.AddProduct) },
//                    navigateAddConfirm = { navigation.navigate(Confirm) },
//                )
//            }
        }
    }
}