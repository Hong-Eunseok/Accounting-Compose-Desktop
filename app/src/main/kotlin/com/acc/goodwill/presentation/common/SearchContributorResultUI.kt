package com.acc.goodwill.presentation.common

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.seed
import com.acc.goodwill.domain.model.Contributor

@Composable
fun SearchContributorResultUI(
    searchResult: List<Contributor>,
    selectedContributor: (Contributor) -> Unit,
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
                        .background(Color.White)
                        .selectable(selected = false, onClick = { selectedContributor(contributor) })
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
                        ) {
                            AppIcon(imageVector = Icons.Default.KeyboardArrowRight, tint = seed)
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