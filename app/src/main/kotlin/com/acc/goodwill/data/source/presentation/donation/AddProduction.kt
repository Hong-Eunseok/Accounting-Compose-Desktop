package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.AppTextField
import com.acc.goodwill.data.source.presentation.navigation.SearchContribute
import com.acc.goodwill.domain.model.rememberProduct

@Preview
@Composable
fun AddProduct() {
    var showCompanies by remember { mutableStateOf(false) }
    val product = rememberProduct()

    Column {
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = product.category,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h3
                )
            }
            Column(modifier = Modifier) {
                Box {
                    OutlinedButton(
                        onClick = { showCompanies = true },
                        modifier = Modifier
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier) {
                            Text(text = "Category")
                            AppIcon(imageVector = if (showCompanies) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown)
                        }
                    }
                    DropdownMenu(
                        expanded = showCompanies,
                        onDismissRequest = { showCompanies = false }
                    ) {
                        listOf(
                            "의류",
                            "도서",
                            "생활",
                            "유아",
                            "문구,완구",
                            "가구",
                            "가전",
                            "주방",
                            "기타"
                        ).forEachIndexed { index, value ->
                            DropdownMenuItem(onClick = {
                                showCompanies = false
                                product.category = value
                            }) {
                                Text(text = value)
                            }
                        }
                    }
                }
            }
        }

        AppTextField(
            value = product.label,
            setValue = { product.label = it },
            label = "기증품목",
            singleLine = true
        )

        AppTextField(
            value = product.total,
            setValue = { value ->
                product.total = value.filter { it.isDigit() }
            },
            label = "전체 수량",
            singleLine = true
        )

        AppTextField(
            value = product.error,
            setValue = { product.error = it },
            label = "불량",
            singleLine = true
        )

        AppTextField(
            value = product.price,
            setValue = { product.price = it },
            label = "금액",
            singleLine = true
        )

        Button(
            onClick = {},
            modifier = Modifier.padding(8.dp),
        ) {
            Text(text = "추가")
        }

//        AppTextField(
//            value = test,
//            setValue = { test = it },
//            label = "기증품목",
//            singleLine = true
//        )

//        AppTextField(
//            value = test,
//            setValue = { test = it },
//            label = "수량",
//            singleLine = true
//        )
    }
}


//카테고리 : 의류/도서/잡화생활/잡화유아/문구,완구/가구/가전/주방/기타
//기증품목 : XXXXXX
//수량 : XXXXXX
//불량 : XXXX
//금액 : XXXX
//추가
//
//다음
//
//추가된 리스트 보이고
//뭔가 이쁘게
