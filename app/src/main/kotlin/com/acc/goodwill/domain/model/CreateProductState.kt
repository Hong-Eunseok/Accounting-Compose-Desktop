package com.acc.goodwill.domain.model

import androidx.compose.runtime.*


@Composable
fun rememberProduct(): CreateProductState {
    return remember { CreateProductState() }
}

class CreateProductState {
    var category by mutableStateOf("선택하세요")
    var label by mutableStateOf("")
    var total by mutableStateOf("")
    var error by mutableStateOf("")
    var price by mutableStateOf("")
    var selectedIndex by mutableStateOf(-1)

    val valid by derivedStateOf {
        when {
            selectedIndex == -1 -> Validate.CATEGORY
            label.isEmpty() -> Validate.LABEL
            total.isEmpty() -> Validate.TOTAL
            error.isEmpty() -> Validate.ERROR
            price.isEmpty() -> Validate.PRICE
            total.toInt() < error.toInt() -> Validate.WRONG_TOTAL
            else -> Validate.VALID
        }
    }

    fun init() {
        category = "선택하세요"
        label = ""
        total = ""
        error = ""
        price = ""
        selectedIndex = -1
    }

    enum class Validate {
        VALID, LABEL, TOTAL, ERROR, PRICE, WRONG_TOTAL, CATEGORY
    }
}