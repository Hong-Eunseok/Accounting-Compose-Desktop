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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.*
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
        navigateModifyContributor: (Contributor) -> Unit,
        selectedContributor: (Contributor) -> Unit,
        searchResult: List<Contributor>,
        searchKeyword: (String) -> Unit
    ) {

        var keyword by remember { mutableStateOf("") }
        var selectedIndex by remember { mutableStateOf(-1) }

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
                    navigateModifyContributor = navigateModifyContributor,
                    searchResult = searchResult,
                    selectedIndex = selectedIndex,
                    setValue = { selectedIndex = if (selectedIndex != it) it else -1 }
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Button(
                        onClick = { selectedContributor(searchResult[selectedIndex]) },
                        enabled = selectedIndex != -1,
                        modifier = Modifier.padding(bottom = 100.dp)
                    ) {
                        Text("다음")
                    }
                }
            }

        }
    }

    @Composable
    private fun LazyScrollable(
        navigateModifyContributor: (Contributor) -> Unit,
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
            modifier = Modifier.fillMaxHeight(fraction = 0.8f).padding(10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("이름 : ${contributor.name}")
                                Text("번호 : ${contributor.phoneNumber.orEmpty().ifEmpty { "정보가 없습니다." }}")
                                Text("주민/사업자 번호 : ${contributor.registrationNumber.orEmpty().ifEmpty { "정보가 없습니다." }}")
                                Text("주소 : ${contributor.address.orEmpty().ifEmpty { "정보가 없습니다." }}")
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Box(
                                contentAlignment = Alignment.CenterEnd,
                                modifier = Modifier.clickable { navigateModifyContributor(contributor) }
                            ) {
                                AppIcon(imageVector = Icons.Default.Edit, tint = seed)
                            }
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
