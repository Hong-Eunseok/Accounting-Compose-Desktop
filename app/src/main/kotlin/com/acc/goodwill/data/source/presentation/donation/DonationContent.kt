package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
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
import com.acc.goodwill.domain.model.TodayDonate

@Composable
fun DonationContent(
    navigateAddDonation: () -> Unit,
    todayDonations: List<TodayDonate>
) {
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
                "금일 기부수 ${todayDonations.size}건",
                style = MaterialTheme.typography.h1,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                "총 기부금액 ${todayDonations.sumOf { it.donate.price }}원",
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

            TodayDonation(todayDonations)
        }
    }
}

@Composable
fun TodayDonation(
    todayDonations: List<TodayDonate>,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(end = 12.dp),
        state = state
    ) {
        items(todayDonations.size) { index ->
            val todayDonate = todayDonations[index]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
//                    .background(if (selectedIndex == index) MaterialTheme.colors.primary else Color.White)
                    .selectable(
                        selected = false,
                        onClick = { /*setValue(index)*/ }
                    )
                    .padding(mediumPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "총수량 / 불량 / 양품 : ${todayDonate.donate.total} / ${todayDonate.donate.error} / ${todayDonate.donate.correct}",
                            style = MaterialTheme.typography.body2,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                            modifier = Modifier
                        )
                        Text(
                            text = if (todayDonate.contributor == null) {
                                "무명"
                            } else {
                                "이름 : ${todayDonate.contributor.name} | 번호 : ${todayDonate.contributor.phoneNumber}"
                            },
                            style = MaterialTheme.typography.h3,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                            modifier = Modifier
                        )
                        Text(
                            "기부환산금액 : ${todayDonate.donate.price}원",
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
                        AppIcon(imageVector = Icons.Default.KeyboardArrowRight, tint = seed)
                    }
                }

            }
            Spacer(modifier = Modifier.height(largePadding))
        }
    }
}

