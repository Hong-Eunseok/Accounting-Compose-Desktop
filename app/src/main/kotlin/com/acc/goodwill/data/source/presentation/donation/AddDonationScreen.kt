package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.navigation.*
import com.acc.goodwill.domain.model.Contributor
import com.navigation.rememberNavigation
import javax.inject.Inject

class AddDonationScreen(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    private val searchContributorContent by lazy { SearchContributorContent(appComponent) }
    init {
        appComponent.inject(this)
    }

    @Composable
    fun AddDonationScreen(
        navigateBack: () -> Unit,
        navigateAddContributor: () -> Unit,
    ) {

        val navigation = rememberNavigation(defaultRoute = SearchContribute)
        val route by navigation.routeStack.collectAsState()

        val searchResult by viewModel.searchResult.collectAsState()
        var contributor by remember { mutableStateOf(Contributor.INIT) }


        var screenTitle by remember { mutableStateOf("기부자 찾기") }
        var price by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = screenTitle, style = MaterialTheme.typography.h4)
                        if (contributor.validUser()) {
                            Spacer(modifier = Modifier.width(200.dp))
                            Text(
                                text = "${contributor.name} | ${contributor.phoneNumber}",
                                style = MaterialTheme.typography.h3
                            )
                            if (price.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(text = price, style = MaterialTheme.typography.h3)
                            }
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
                        navigateSearchContributor = {
                            contributor = Contributor.INIT
                            screenTitle = "기부자 찾기"
                            viewModel.clearSearchContributor()
                            navigation.navigate(SearchContribute)
                        },
                        navigateAddProduct = {
                            screenTitle = "물품 추가하기"
                            navigation.navigate(AddProduct)
                        },
                        navigateAddConfirm = {
                            price = "5000원"
                            navigation.navigate(Confirm)
                        },
                    )
                    // content side
                    AddDonationContent(modifier = Modifier.weight(3f)) {
                        when (route) {
                            SearchContribute -> searchContributorContent.SearchContributorContent(
                                navigateAddContributor = navigateAddContributor,
                                selectedContributor = {
                                    contributor = it
                                    screenTitle = "물품 추가하기"
                                    navigation.navigate(AddProduct)
                                },
                                searchResult = searchResult,
                                searchKeyword = viewModel::searchContributor
                            )
                            AddProduct -> AddProductContent { navigation.navigate(Confirm) }
                            Confirm -> ConfirmDonationContent { navigateBack() }
                        }
                    }
                }
            }
        }

    }

}



