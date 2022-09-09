package com.acc.goodwill.domain.model

data class Contributor(
    val name: String,
    val phoneNumber: String?,
    val address: String?,
    val registrationNumber: String?,
    val recommend: Int
)