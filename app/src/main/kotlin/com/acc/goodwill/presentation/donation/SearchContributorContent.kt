package com.acc.goodwill.presentation.donation

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
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.*
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import java.awt.Dimension
import javax.inject.Inject
import javax.swing.JPanel
import javax.swing.JTextField


class SearchContributorContent(appComponent: AppComponent) {
    @Inject lateinit var viewModel: DonationViewModel

    private val searchFiled: JTextField = JTextField("")

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
        val rgb: Int = MaterialTheme.colors.background.toArgb()
        var selectedIndex by remember { mutableStateOf(-1) }
        val density = LocalDensity.current
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(40.dp)
                            .onSizeChanged {
                                var width: Int
                                var height: Int
                                with(density) {
                                    width = it.width.toDp().value.toInt()
                                    height = it.height.toDp().value.toInt()
                                }
                                searchFiled.preferredSize = Dimension(width, height)
                            }
                    ) {
                        SwingPanel(
                            factory = {
                                JPanel().apply {
                                    background = java.awt.Color(rgb)
                                    add(searchFiled.apply {
                                        addActionListener {
                                            if (searchFiled.text.isNotEmpty()) searchKeyword(searchFiled.text)
                                        }
                                    })
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(smallerPadding))
                    Button(
                        onClick = { if (searchFiled.text.isNotEmpty()) searchKeyword(searchFiled.text) },
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
