package com.acc.goodwill.domain.model

data class Product(
    val category: String,
    val label: String,
    val total: Int,
    val error: Int,
    val price: Long,
    val correct: Int = total - error,
    val transferPrice: Long = correct * price
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