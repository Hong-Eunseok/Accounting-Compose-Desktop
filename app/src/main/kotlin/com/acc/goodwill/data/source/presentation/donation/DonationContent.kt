package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon

@OptIn(ExperimentalMaterialApi::class)
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
        Column {
            Row(
                modifier = Modifier.padding(bottom = 15.dp)
            ) {
                Text(
                    "금일 기부수 3건",
                    style = MaterialTheme.typography.h6,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                )
                Text(
                    "3" + "건",
                    style = MaterialTheme.typography.h6,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                )
            }
            Text(
                "기부금액 5000원",
                style = MaterialTheme.typography.body2,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            )

            Text(
                "기부내역",
                style = MaterialTheme.typography.body2,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            )
            Card {
                Column {
                    ListItem(
                        overlineText = { Text(text = "총수량/불량/양품 : 10 / 3 / 7")  },
                        secondaryText = { Text(text = "기부환산금액 : 10000원") }
                    ) {
                        Text("이름 : 홍길동 | 번호 : 010-3020-6909")
                    }
                    Divider()
                    ListItem(
                        overlineText = { Text(text = "총수량/불량/양품 : 10 / 3 / 7")  },
                        secondaryText = { Text(text = "기부환산금액 : 10000원") }
                    ) {
                        Text("이름 : 홍길동 / 번호 : 010-3020-6909")
                    }
                    Divider()
                    ListItem(
                        overlineText = { Text(text = "총수량/불량/양품 : 10 / 3 / 7")  },
                        secondaryText = { Text(text = "기부환산금액 : 10000원") }
                    ) {
                        Text("이름 : 홍길동 / 번호 : 010-3020-6909")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DonationContentPreview() {
    DonationContent({})
}