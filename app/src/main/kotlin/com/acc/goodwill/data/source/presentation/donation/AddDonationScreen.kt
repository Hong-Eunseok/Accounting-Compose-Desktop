package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.smallPadding
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.navigation.*
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.DatabaseResult
import com.acc.goodwill.domain.model.Product
import com.navigation.rememberNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class AddDonationScreen(appComponent: AppComponent) {

    @Named("main") @Inject lateinit var mainScope: CoroutineScope
    @Inject lateinit var viewModel: DonationViewModel

    private val searchContributorContent by lazy { SearchContributorContent(appComponent) }
    init {
        appComponent.inject(this)
    }

    @Composable
    fun AddDonationScreen(
        navigateBack: () -> Unit,
        navigateAddContributor: () -> Unit,
        navigateModifyContributor: (Contributor) -> Unit,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val navigation = rememberNavigation(defaultRoute = SearchContribute)
        val route by navigation.routeStack.collectAsState()
        val searchResult by viewModel.searchResult.collectAsState()
        var contributor by remember { mutableStateOf(Contributor.INIT) }
        var products by remember { mutableStateOf(listOf<Product>()) }

        var screenTitle by remember { mutableStateOf("1. 기부자 찾기") }
        var price by remember { mutableStateOf("") }

        val addDonationResult by viewModel.addDonationResult.collectAsState()

        when (addDonationResult) {
            DatabaseResult.SUCCESS -> "등록에 성공하였습니다."
            DatabaseResult.ERROR -> "등록에 실패하였습니다."
            else -> null
        }?.let { message ->
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message)
                navigateBack()
            }
        }

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
                                Spacer(modifier = Modifier.width(smallPadding))
                                Text(text = price, style = MaterialTheme.typography.h3)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            },
            scaffoldState = scaffoldState
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
                            products = listOf()
                            viewModel.clearSearchContributor()
                            navigation.navigate(SearchContribute)
                        },
                        navigateAddProduct = {},
                        navigateAddConfirm = {},
                    )
                    // content side
                    AddDonationContent(modifier = Modifier.weight(3f)) {
                        when (route) {
                            SearchContribute -> searchContributorContent.SearchContributorContent(
                                navigateAddContributor = navigateAddContributor,
                                navigateModifyContributor = navigateModifyContributor,
                                selectedContributor = {
                                    contributor = it
                                    navigation.navigate(AddProduct)
                                },
                                searchResult = searchResult,
                                searchKeyword = viewModel::searchContributor
                            )

                            AddProduct -> AddProductContent(
                                navigateNext = { navigation.navigate(Confirm) },
                                deleteProduct = {
                                    val newValue = products.toMutableList()
                                    newValue.remove(it)
                                    products = newValue
                                },
                                addProduct = { product ->
                                    val newValue = products.toMutableList().apply { add(product) }
                                    products = newValue
                                },
                                products = products
                            )

                            Confirm -> ConfirmDonationContent(
                                products = products,
                                navigateBack = { navigation.popLast() },
                                navigateConfirm = {
                                    viewModel.addDonation(contributor, products, it)
                                }
                            )
                        }
                    }
                }

                when (route) {
                    SearchContribute -> "1. 기부자 찾기"
                    AddProduct -> {
                        price = ""
                        "2. 기부물품등록"
                    }
                    Confirm -> {
                        price = "${products.sumOf { it.transferPrice }}원"
                        "3. 최종확인"
                    }
                    else -> null
                }?.run { screenTitle = this }
            }
        }

    }

    @Composable
    private fun AddDonationContent(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxHeight().background(Color.Gray)) {
            content()
        }
    }
}




