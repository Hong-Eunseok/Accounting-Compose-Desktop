package com.acc.goodwill.domain.model

data class Product(
    val category: Int,
    val label: String,
    val total: UInt,
    val error: UInt,
    val price: ULong,
    val correct: UInt = total - error,
    val transferPrice: ULong = correct * price
) {
    companion object {
        val CATEGORIES = listOf(
            "의류",
            "도서",
            "생활",
            "유아",
            "문구,완구",
            "가구",
            "가전",
            "주방",
            "기타"
        )
    }
}