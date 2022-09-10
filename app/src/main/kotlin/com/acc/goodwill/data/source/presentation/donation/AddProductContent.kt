package com.acc.goodwill.data.source.presentation.donation

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
import com.acc.common.components.RowTextField
import com.acc.common.ui.largePadding
import com.acc.goodwill.domain.model.CreateProductState
import com.acc.goodwill.domain.model.rememberProduct

@Composable
fun AddProductContent(navigateConfirm: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Column(
            modifier = Modifier.padding(start = largePadding, end = largePadding, top = largePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductForm()

            ProductResult()

            Row {
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "카테고리", modifier = Modifier.widthIn(min = 120.dp))

            Spacer(modifier = Modifier.width(largePadding))

            Box {
                OutlinedButton(
                    onClick = { showCompanies = true },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier) {
                        Text(text = product.category)
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
                        DropdownMenuItem(
                            onClick = {
                                showCompanies = false
                                product.category = value
                                product.selectedIndex = index
                            }
                        ) {
                            Text(text = value)
                        }
                    }
                }
            }
        }

        RowTextField(
            value = product.label,
            setValue = { product.label = it },
            label = "기증품목",
            modifier = Modifier.padding(bottom = largePadding),
            errorMessage = takeIf { product.valid == CreateProductState.Validate.LABEL }
                ?.run { "기증 품목을 입력해주세요." },
            deleteLastChar = { product.label = product.label.substring(0 until product.label.length - 1) }
        )

        RowTextField(
            value = product.total,
            setValue = { value ->
                if (value.length <= 9) product.total = value.filter { it.isDigit() }
            },
            label = "수량",
            modifier = Modifier.padding(bottom = largePadding),
            errorMessage = when (product.valid) {
                CreateProductState.Validate.TOTAL -> "수량을 입력해주세요."
                CreateProductState.Validate.WRONG_TOTAL -> "수량과 불량수를 확인해주세요."
                else -> null
            }
        )

        RowTextField(
            value = product.error,
            setValue = { value -> if (value.length <= 9) product.error = value.filter { it.isDigit() } },
            label = "불량",
            modifier = Modifier.padding(bottom = largePadding),
            errorMessage = when (product.valid) {
                CreateProductState.Validate.ERROR -> "불량을 입력해주세요."
                CreateProductState.Validate.WRONG_TOTAL -> "수량과 불량을 확인해주세요."
                else -> null
            }
        )

        RowTextField(
            value = product.price,
            setValue = { value -> if (value.length <= 9) product.price = value.filter { it.isDigit() } },
            label = "가격",
            modifier = Modifier.padding(bottom = largePadding),
            errorMessage = takeIf { product.valid == CreateProductState.Validate.PRICE }
                ?.run { "가격을 입력해주세요." }
        )

        Spacer(modifier = Modifier.height(largePadding))

        Button(
            onClick = { product.init() },
            modifier = Modifier.widthIn(min = 200.dp),
            enabled = product.valid == CreateProductState.Validate.VALID
        ) {
            Text("추가")
        }
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