package com.acc.goodwill.domain.model

import androidx.compose.runtime.*


@Composable
fun rememberProduct(): CreateProductState {
    return remember { CreateProductState() }
}

class CreateProductState {
    var category by mutableStateOf("Select")
    var label by mutableStateOf("")
    var total by mutableStateOf("")
    var error by mutableStateOf("")
    var price by mutableStateOf("")
}