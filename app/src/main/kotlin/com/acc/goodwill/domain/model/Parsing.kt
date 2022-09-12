package com.acc.goodwill.domain.model

import java.time.LocalDateTime

data class Parsing(
    val createAt: LocalDateTime,
    val contributor: Contributor?,
    val donation: Donation,
    val products: List<Product>,
) {
    data class Contributor(
        val name: String,
        val phoneNumber: String? = null,
        val address: String? = null,
        val registrationNumber: String? = null,
        val recommend: Int = 0,
        val registrationType: Int = 0,
    )

    data class Donation(
        val total: Int,
        val total_error: Int,
        val total_correct: Int,
        val price: ULong,
        val fromType: Int,
        val optional: String,
        val organization: Int = 0,
        val member: Boolean = true
    ) {
        companion object {
            val FROM_TYPE = listOf("기증", "수거", "기타")
        }
    }

    data class Product(
        val category: Int,
        val total: Int,
        val error: Int,
        val correct: Int,
        val transferPrice: ULong = 0u,
        val price: ULong = 0u,
        val label: String = com.acc.goodwill.domain.model.Product.CATEGORIES[category]
    )

}