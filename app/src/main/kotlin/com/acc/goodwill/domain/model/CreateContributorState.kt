package com.acc.goodwill.domain.model

import androidx.compose.runtime.*

@Composable
fun rememberContributor(): CreateContributorState {
    return remember { CreateContributorState() }
}

class CreateContributorState {
    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var address by mutableStateOf("")
    var registrationNumber by mutableStateOf("")
    var join by mutableStateOf(0)

    val valid by derivedStateOf { name.isNotEmpty() }

    fun init() {
        name = ""
        phoneNumber = ""
        address = ""
        registrationNumber = ""
        join = 0
    }
}