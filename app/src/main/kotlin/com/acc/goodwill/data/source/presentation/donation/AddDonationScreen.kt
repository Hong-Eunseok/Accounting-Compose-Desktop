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
import com.acc.goodwill.data.source.presentation.navigation.*
import com.navigation.rememberNavigation

@Composable
fun AddDonationScreen(
    navigateBack: () -> Unit,
    navigateAddContributor: () -> Unit,
) {

    val navigation = rememberNavigation(defaultRoute = SearchContribute)
    val route by navigation.routeStack.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "기부 추가하기", style = MaterialTheme.typography.h4) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                }
            )
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val screenWidth = constraints.maxWidth
            Row(Modifier.fillMaxWidth()) {
                // menu side
                AddDonationMenu(
                    route as DonationRoute,
                    screenWidth.dp,
                    navigateSearchContributor = { navigation.navigate(SearchContribute) },
                    navigateAddProduct = { navigation.navigate(AddProduct) },
                    navigateAddConfirm = { navigation.navigate(Confirm) },
                )
                // content side
                AddDonationContent(modifier = Modifier.weight(3f)) {
                    when (route) {
                        SearchContribute -> SearchContributorContent(navigateAddContributor)
                        AddProduct -> EmptyScreen()
                        Confirm -> EmptyScreen()
                        AddContributor -> AddContributorScreen { navigation.popLast() }
                    }
                }
            }
        }
    }

}

@Composable
fun EmptyScreen() {
    Scaffold {

    }
}