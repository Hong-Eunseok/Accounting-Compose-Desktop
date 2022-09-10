package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.acc.common.components.AppIcon
import com.acc.common.components.RowTextField
import com.acc.common.ui.largePadding
import com.acc.goodwill.domain.model.rememberProduct

@Composable
fun AddProductContent(navigateConfirm: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
    ) {
        Column {
            Row {
                ProductForm()
                ProductResult()
            }

            Row {
                Button(onClick = {}) {
                    Text("추가")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {}) {
                    Text("삭제")
                }
                Button(onClick = {}) {
                    Text("완료")
                }
            }
        }
    }
}

@Composable
fun ProductForm() {
    var showCompanies by remember { mutableStateOf(false) }
    val product = rememberProduct()

    var label by mutableStateOf("")
    var count by mutableStateOf("")
    var errorCount by mutableStateOf("")
    var price by mutableStateOf("")

    Column {
        Row {
            Text("카테고리")
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

        RowTextField(
            value = label,
            setValue = { label = it },
            label = "기증품목",
            modifier = Modifier.padding(bottom = largePadding)
        )

        RowTextField(
            value = count,
            setValue = { value -> count = value.filter { it.isDigit() } },
            label = "수량",
            modifier = Modifier.padding(bottom = largePadding)
        )

        RowTextField(
            value = errorCount,
            setValue = { value -> errorCount = value.filter { it.isDigit() } },
            label = "불량",
            modifier = Modifier.padding(bottom = largePadding)
        )

        RowTextField(
            value = price,
            setValue = { value -> price = value.filter { it.isDigit() } },
            label = "가격",
            modifier = Modifier.padding(bottom = largePadding)
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductResult() {
    Column(modifier = Modifier.fillMaxWidth(1f)) {
        Text("물품 추가 목록")
        ListItem(
            trailing = { RadioButton(false, onClick = { }) },
            overlineText = { Text("카테고리 : 잡화") },
            secondaryText = { Text("기부 환산금액 : 50000") }
        ) {
            Column {
                Text("기증 품목 : 자동차")
                Text(text = "총수량/불량/양품 : 10 / 3 / 7")
            }
        }
        Divider()
        ListItem(
            trailing = { RadioButton(false, onClick = { }) },
            overlineText = { Text("카테고리 : 잡화") },
            secondaryText = { Text("기부 환산금액 : 50000") }
        ) {
            Column {
                Text("기증 품목 : 자동차")
                Text(text = "총수량/불량/양품 : 10 / 3 / 7")
            }
        }
    }
}