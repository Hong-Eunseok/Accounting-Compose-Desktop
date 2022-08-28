package com.acc.goodwill.presentation.common

interface Locale {
    /** Home Menu **/
    val donation: String
    val search: String
    val report: String
}

object Korean : Locale {
    /** Home Menu **/
    override val donation: String = "기부"
    override val search: String = "검색"
    override val report: String = "보고서"
}

object English : Locale {
    /** Home Menu **/
    override val donation: String = "Donation"
    override val search: String = "Search"
    override val report: String = "Report"
}