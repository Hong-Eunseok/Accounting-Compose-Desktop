package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.AppTextField
import com.acc.common.ui.largePadding
import com.acc.common.ui.smallPadding
import com.acc.di.AppComponent
import com.acc.di.DaggerAppComponent
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.rememberContributor
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.navigation.AddProduct
import com.acc.goodwill.data.source.presentation.navigation.Confirm
import com.acc.goodwill.data.source.presentation.navigation.DonationRoute
import com.acc.goodwill.data.source.presentation.navigation.SearchContribute
import com.navigation.rememberNavigation
import javax.inject.Inject

class AddDonationContent(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    fun addDonationContent(navigateBack: () -> Unit) {
        val navigation = rememberNavigation(defaultRoute = SearchContribute)
        val route by navigation.routeStack.collectAsState()

        val locale = LocaleComposition.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = locale.addDonation, style = MaterialTheme.typography.h4) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            }
        ) {
            Column {
                AddDonationMenu(
                    route as DonationRoute,
                    { navigation.navigate(SearchContribute) },
                    { navigation.navigate(AddProduct) },
                    { navigation.navigate(AddProduct) }
                )

                Content(modifier = Modifier) {
                    when (route) {
                        SearchContribute -> SearchLayout()
                        AddProduct -> AddProduct()
                        Confirm -> AddContribute()
                    }
                }
            }
        }
    }

    @Composable
    fun Content(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.background(Color.Gray)
        ) {
            content()
        }
    }


    @Composable
    fun SearchLayout() {
        val locale = LocaleComposition.current
        var search by remember { mutableStateOf(TextFieldValue("")) }
        val searchResult = viewModel.searchResult.collectAsState(initial = listOf())

        Card(modifier = Modifier.width(500.dp).padding(top = 10.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = search,
                        leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "Phone & Name") },
                        keyboardActions = KeyboardActions(onSearch = { println("keyboard enter") }),
                        modifier = Modifier.padding(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        label = { Text(text = locale.nameOrPhoneNumber) },
                        placeholder = { Text(text =locale.hintSearch) },
                        onValueChange = { search = it },
                    )
                    Button(
                        onClick = { viewModel.searchContributor(search.text) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "검색")
                    }
                }

                SearchResult(searchResult.value)
            }
        }
    }




    @Composable
    fun SearchResult(contributors: List<Contributor>) {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            item { Text(text = "검색 결과") }
            itemsIndexed(contributors) { index, item ->
                Row(modifier = Modifier.clickable { println("oooo $index") }) {
                    Text(text = "Item: ${contributors[index]}")
                }
            }
            item { Text(text = "마지막 입니다.") }
        }
    }

    @Composable
    fun AddContribute() {
        val locale = LocaleComposition.current
        val result by viewModel.result.collectAsState()
        val contributor = rememberContributor()

        if (result == CreateContributorResult.SUCCESS) contributor.init()

        Card(modifier = Modifier.width(500.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(smallPadding),
                modifier = Modifier.padding(horizontal = largePadding, vertical = smallPadding)
            ) {
                AppTextField(
                    value = contributor.name,
                    setValue = { contributor.name = it },
                    label = locale.name,
                    singleLine = true
                )
                AppTextField(
                    value = contributor.phoneNumber,
                    setValue = { contributor.phoneNumber = it },
                    label = locale.phoneNumber,
                    singleLine = true
                )
                AppTextField(
                    value = contributor.registrationNumber,
                    setValue = { contributor.registrationNumber = it },
                    label = locale.registrationNumber,
                    singleLine = true
                )
                AppTextField(
                    value = contributor.address,
                    setValue = { contributor.address = it },
                    label = locale.address,
                    singleLine = true
                )
                Row {
                    Row {
                        RadioButton(
                            selected = contributor.join == 0,
                            onClick = { contributor.join = 0 }
                        )
                        Text(text = "교인")
                    }
                    Row {
                        RadioButton(
                            selected = contributor.join == 1,
                            onClick = { contributor.join = 1 }
                        )
                        Text(text = "친구추천")
                    }
                    Row {
                        RadioButton(
                            selected = contributor.join == 2,
                            onClick = { contributor.join = 2 }
                        )
                        Text(text = "인터넷")
                    }
                    Row {
                        RadioButton(
                            selected = contributor.join == 3,
                            onClick = { contributor.join = 3 }
                        )
                        Text(text = "기타")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.padding(end = 10.dp),
                        enabled = !contributor.valid,
                        onClick = {  }
                    ) {
                        Text(text = locale.unknown)
                    }

                    Button(
                        enabled = contributor.valid,
                        onClick = {
                            viewModel.addContributor(
                                contributor.name,
                                contributor.phoneNumber,
                                contributor.address,
                                contributor.registrationNumber,
                                contributor.join
                            )
                        }
                    ) {
                        Text(text = locale.add)
                    }
                }

                if (result == CreateContributorResult.ERROR) {
                    Text(text = "error", color = com.acc.common.ui.error)
                }
            }
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    val appComponent: AppComponent = DaggerAppComponent.create()
    val screen = AddDonationContent(appComponent)
    screen.addDonationContent { }
}
