package com.acc.goodwill.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.AppRowActionsWithoutModify
import com.acc.common.components.RowTextField
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.goodwill.domain.model.CreateProductState
import com.acc.goodwill.domain.model.Product
import com.acc.goodwill.domain.model.rememberProduct

@Composable
fun AddProductContent(
    navigateNext: () -> Unit,
    deleteProduct: (Product) -> Unit,
    addProduct: (Product) -> Unit,
    products: List<Product>
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Column(modifier = Modifier.padding(largePadding),) {

            ProductForm(addProduct)

            Spacer(modifier = Modifier.height(largePadding))

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("물품 추가 목록")
                    Spacer(modifier = Modifier.height(largePadding))

                    if (products.isEmpty()) {
                        Text("등록된 물품이 없습니다.")
                    } else {
                        ProductResult(
                            selectedIndex = selectedIndex,
                            setValue = {
                                selectedIndex = if (selectedIndex == it) -1 else it
                            },
                            products = products,
                            deleteProduct = deleteProduct
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navigateNext() },
                    enabled = products.isNotEmpty()
                ) {
                    Text("다음")
                }
            }
        }
    }
}

@Composable
fun ProductForm(addProduct: (Product) -> Unit) {
    var showCompanies by remember { mutableStateOf(false) }
    val product = rememberProduct()

    Column {
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
                    Product.CATEGORIES.forEachIndexed { index, value ->
                        DropdownMenuItem(
                            onClick = {
                                showCompanies = false
                                product.setCategoryWithIndex(value, index)
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
                ?.run { "가격을 입력해주세요." },
            enterAction = {
                if (product.valid == CreateProductState.Validate.VALID) {
                    addProduct(product.toProduct())
                    product.init()
                }
            }
        )

        Spacer(modifier = Modifier.height(largePadding))

        Button(
            onClick = { addProduct(product.toProduct()); product.init() },
            modifier = Modifier.widthIn(min = 200.dp),
            enabled = product.valid == CreateProductState.Validate.VALID
        ) {
            Text("추가")
        }
    }
}

@Composable
fun ProductResult(
    selectedIndex: Int,
    setValue: (Int) -> Unit,
    products: List<Product>,
    deleteProduct: ((Product) -> Unit)? = null,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(end = 12.dp),
        state = state
    ) {
        items(products.size) { index ->
            val product = products[index]
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
                        Text("품목 : ${product.label}(${Product.CATEGORIES[product.category]})")
                        Text("수량 : ${product.total}")
                        Text("불량/양품 : ${product.error} / ${product.correct}")
                        Text("기부환산금액 : ${product.transferPrice}원 = ${product.correct} * ${product.price}")
                    }
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppRowActionsWithoutModify(
                            selected = index == selectedIndex,
                            onDelete = {
                                if (deleteProduct != null) deleteProduct(products[selectedIndex])
                                setValue(-1)
                            }
                        )
                    }

                }

            }
            Spacer(modifier = Modifier.height(largePadding))
        }
    }


}