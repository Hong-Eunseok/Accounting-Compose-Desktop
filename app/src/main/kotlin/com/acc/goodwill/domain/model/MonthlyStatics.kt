package com.acc.goodwill.domain.model


data class DonationStatics(val donation: Donate, val products: List<Product>)

data class MonthlyStatics(val month: Int, val statics: List<DonationStatics>)