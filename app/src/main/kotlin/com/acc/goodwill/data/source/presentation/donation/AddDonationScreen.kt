package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
    var screenTitle by remember { mutableStateOf("기부 추가하기") }
    var contributor by remember { mutableStateOf("") }

    var phoneNumber by mutableStateOf("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = screenTitle, style = MaterialTheme.typography.h4)
                    if (contributor.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(text = contributor, style = MaterialTheme.typography.h3)
                    }
                },
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
                    navigateAddProduct = {
                        screenTitle = "물품 추가하기"
                        contributor = "홍은석 | 010-3020-6909"
                        navigation.navigate(AddProduct)
                    },
                    navigateAddConfirm = { navigation.navigate(Confirm) },
                )
                // content side
                AddDonationContent(modifier = Modifier.weight(3f)) {
                    when (route) {
                        SearchContribute -> SearchContributorContent(navigateAddContributor)
                        AddProduct -> AddProductContent { navigation.navigate(Confirm) }
                        Confirm -> EmptyScreen()
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