package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.smallPadding
import com.acc.common.ui.smallerPadding
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import javax.inject.Inject

class SearchContributorContent(appComponent: AppComponent) {
    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SearchContributorContent(
        navigateAddContributor: () -> Unit,
        selectedContributor: (Contributor) -> Unit,
        searchResult: List<Contributor>,
        searchKeyword: (String) -> Unit
    ) {

        var keyword by remember { mutableStateOf("") }
        var selectedIndex by mutableStateOf(-1)

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            floatingActionButton = {
                FloatingActionButton(onClick = navigateAddContributor) {
                    AppIcon(imageVector = Icons.Default.Add)
                }
            }
        ) {
            Column(modifier = Modifier.padding(start = largePadding, end = largePadding)) {
                Box(
                    Modifier
                        .requiredHeight(80.dp)
                        .align(Alignment.Start)
                        .padding(top = largePadding)
                ) {
                    Button(onClick = { selectedContributor(Contributor.UNKNOWN) }) {
                        Text("무명으로 등록하기")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = keyword,
                        onValueChange = { keyword = it },
                        enabled = true,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onBackground),
                        modifier = Modifier
                            .border(
                                width = 1.25.dp,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(smallPadding)
                            .weight(0.3f)
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key != Key.Enter) return@onKeyEvent false
                                if (keyEvent.type == KeyEventType.KeyUp && keyword.isNotEmpty()) {
                                    searchKeyword(keyword)
                                    true
                                } else {
                                    false
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(smallerPadding))
                    Button(
                        onClick = { searchKeyword(keyword) },
                        enabled = keyword.isNotEmpty()
                    ) {
                        Text("검색")
                    }
                }
                Text(
                    text = "이름 또는 번호로 검색하기",
                    style = MaterialTheme.typography.caption,
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text("검색 결과")
                Spacer(modifier = Modifier.height(smallerPadding))

                LazyScrollable(
                    searchResult = searchResult,
                    selectedIndex = selectedIndex,
                    setValue = { selectedIndex = if (selectedIndex != it) it else -1 }
                )

                Row(Modifier.weight(0.1f)) {
                    if (selectedIndex != -1) {
                        Button(onClick = {}) {
                            Text("수정하기")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = { selectedContributor(searchResult[selectedIndex]) }) {
                            Text("선택하기")
                        }
                    }
                }
            }

        }
    }

    @Composable
    private fun LazyScrollable(
        searchResult: List<Contributor>,
        selectedIndex: Int,
        setValue: (Int) -> Unit,
        state: LazyListState = rememberLazyListState()
    ) {

        if (searchResult.isEmpty()) {
            Text("검색 결과가 없습니다.")
            return
        }

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(fraction = 0.8f).padding(10.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                state = state,
            ) {
                items(searchResult.size) { index ->
                    val contributor = searchResult[index]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (selectedIndex == index) MaterialTheme.colors.primary else Color.White)
                            .selectable(
                                selected = index == selectedIndex,
                                onClick = { setValue(index) }
                            )
                            .padding(mediumPadding)
                    ) {
                        Column {
                            Text("이름 : ${contributor.name}")
                            Text("번호 : ${contributor.phoneNumber}")
                            Text("주민/사업자 번호 : ${contributor.registrationNumber}")
                        }
                    }
                    Spacer(modifier = Modifier.height(largePadding))
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = state)
            )
        }
    }

}



//class SearchContributorContent(appComponent: AppComponent) {
//
//    @Inject lateinit var viewModel: DonationViewModel
//
//    init {
//        appComponent.inject(this)
//    }
//
//    @Composable
//    fun addDonationContent(navigateBack: () -> Unit) {
//        val navigation = rememberNavigation(defaultRoute = SearchContribute)
//        val route by navigation.routeStack.collectAsState()
//
//        val locale = LocaleComposition.current
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text(text = locale.addDonation, style = MaterialTheme.typography.h4) },
//                    navigationIcon = {
//                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
//                    }
//                )
//            }
//        ) {
//            Column {
////                AddDonationMenu(
////                    route as DonationRoute,
////                    { navigation.navigate(SearchContribute) },
////                    { navigation.navigate(AddProduct) },
////                    { navigation.navigate(AddProduct) }
////                )
//
//                Content(modifier = Modifier) {
//                    when (route) {
//                        SearchContribute -> SearchLayout()
//                        AddProduct -> AddProduct()
//                        Confirm -> AddContribute()
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun Content(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = modifier.background(Color.Gray)
//        ) {
//            content()
//        }
//    }
//
//
//    @Composable
//    fun SearchLayout() {
//        val locale = LocaleComposition.current
//        var search by remember { mutableStateOf(TextFieldValue("")) }
//        val searchResult = viewModel.searchResult.collectAsState(initial = listOf())
//
//        Card(modifier = Modifier.width(500.dp).padding(top = 10.dp)) {
//            Column {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    OutlinedTextField(
//                        value = search,
//                        leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "Phone & Name") },
//                        keyboardActions = KeyboardActions(onSearch = { println("keyboard enter") }),
//                        modifier = Modifier.padding(8.dp),
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                        label = { Text(text = locale.nameOrPhoneNumber) },
//                        placeholder = { Text(text =locale.hintSearch) },
//                        onValueChange = { search = it },
//                    )
//                    Button(
//                        onClick = { viewModel.searchContributor(search.text) },
//                        modifier = Modifier.padding(8.dp)
//                    ) {
//                        Text(text = "검색")
//                    }
//                }
//
//                SearchResult(searchResult.value)
//            }
//        }
//    }
//
//
//
//
//    @Composable
//    fun SearchResult(contributors: List<Contributor>) {
//        val listState = rememberLazyListState()
//        LazyColumn(state = listState) {
//            item { Text(text = "검색 결과") }
//            itemsIndexed(contributors) { index, item ->
//                Row(modifier = Modifier.clickable { println("oooo $index") }) {
//                    Text(text = "Item: ${contributors[index]}")
//                }
//            }
//            item { Text(text = "마지막 입니다.") }
//        }
//    }
//
//
//@Preview
//@Composable
//fun ComposablePreview() {
//    val appComponent: AppComponent = DaggerAppComponent.create()
//    val screen = SearchContributorContent(appComponent)
//    screen.addDonationContent { }
//}