package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.OptionButton
import com.acc.common.components.RowTextField
import com.acc.common.ui.largePadding
import com.acc.common.ui.smallPadding
import com.acc.common.ui.smallerPadding
import com.acc.goodwill.data.source.presentation.navigation.Confirm
import com.acc.goodwill.data.source.presentation.navigation.DonationRoute
import com.acc.goodwill.data.source.presentation.navigation.SearchContribute
import com.navigation.rememberNavigation

@Composable
fun AddContributorScreen(navigateBack: () -> Unit) {

    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var registerNumber by mutableStateOf("")
    var address by mutableStateOf("")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "기부자 정보 입력", style = MaterialTheme.typography.h4) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Card(modifier = Modifier) {
                Column {
                    RowTextField(
                        value = name,
                        setValue = { name = it },
                        label = "이름",
                        modifier = Modifier.padding(bottom = largePadding)
                    )

                    RowTextField(
                        value = phoneNumber,
                        setValue = { phoneNumber = it },
                        label = "연락처",
                        modifier = Modifier.padding(bottom = largePadding)
                    )

                    RowTextField(
                        value = registerNumber,
                        setValue = { registerNumber = it },
                        label = "주민/사업자 번호",
                        modifier = Modifier.padding(bottom = largePadding)
                    )

                    RowTextField(
                        value = address,
                        setValue = { address = it },
                        label = "주소",
                        modifier = Modifier.padding(bottom = largePadding)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "가입경로", modifier = Modifier.padding(end = largePadding))

                        OptionButton("교인")
                        OptionButton("인터넷")
                        OptionButton("지인소개")
                        OptionButton("기타")
                    }

                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = {}) {
                            Text("추가")
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun TextViewFiledRow() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "주민/사업자 번호1",)

        Spacer(modifier = Modifier.width(largePadding))

        BasicTextField(
            value = "주민/사업자 번호",
            onValueChange = { },
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
                .weight(.5f)
        )
    }
}