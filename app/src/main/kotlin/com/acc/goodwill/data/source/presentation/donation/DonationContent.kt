package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.seed

@Composable
fun DonationContent(navigateAddDonation: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        floatingActionButton = {
            FloatingActionButton(onClick = navigateAddDonation) {
                AppIcon(imageVector = Icons.Default.Add)
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = largePadding)
        ) {

            Text(
                "금일 기부수 3건",
                style = MaterialTheme.typography.h1,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                "총 기부금액 5000원",
                style = MaterialTheme.typography.h1,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                "기부내역",
                style = MaterialTheme.typography.h4,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(mediumPadding))

            TodayDonation()
        }
    }
}

@Composable
fun TodayDonation(
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(end = 12.dp),
        state = state
    ) {
        items(300) { index ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
//                    .background(if (selectedIndex == index) MaterialTheme.colors.primary else Color.White)
//                    .selectable(
//                        selected = index == selectedIndex,
//                        onClick = { setValue(index) }
//                    )
                    .padding(mediumPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "총수량 / 불량 / 양품 : 10 / 3 / 7",
                            style = MaterialTheme.typography.body2,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                            modifier = Modifier
                        )
                        Text(
                            "이름 : 홍은석 | 번호 : 010-3020-6909",
                            style = MaterialTheme.typography.h3,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                            modifier = Modifier
                        )
                        Text(
                            "기부환산금액 : 10000원",
                            style = MaterialTheme.typography.body1,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {  }
                            .width(40.dp)
                    ) {
                        AppIcon(imageVector = Icons.Default.Person, tint = seed)
                    }
                }

            }
            Spacer(modifier = Modifier.height(largePadding))
        }
    }
}

//@Composable
//fun d

@Preview
@Composable
fun DonationContentPreview() {
    DonationContent({})
}