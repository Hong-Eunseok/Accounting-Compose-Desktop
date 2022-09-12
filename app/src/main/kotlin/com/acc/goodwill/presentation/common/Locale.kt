package com.acc.goodwill.presentation.common

interface Locale {
    /** Home Menu **/
    val statistics: String
    val donation: String
    val search: String
    val report: String
    val addDonation: String
    /** Add Donation **/
    val stepSearchContributor: String
    val stepAddProduct: String
    val stepConfirm: String
    val name: String
    val phoneNumber: String
    val address: String
    val nameOrPhoneNumber: String
    val hintSearch: String
    val add: String
    val unknown: String
    val registrationNumber: String
}

object Korean : Locale {
    /** Home Menu **/
    override val statistics: String = "통계"
    override val donation: String = "기부"
    override val search: String = "검색"
    override val report: String = "보고서"
    override val addDonation: String = "기부 추가"
    /** Add Donation **/
    override val stepSearchContributor: String = "1. 기부 명단 찾기"
    override val stepAddProduct: String = "2. 기부 물품 추가"
    override val stepConfirm: String = "3. 최종 확인"
    override val name: String = "이름"
    override val phoneNumber: String = "연락처"
    override val address: String = "주소"
    override val nameOrPhoneNumber: String = "연락처 또는 주소 입력으로 찾기"
    override val hintSearch: String = "홍길동 / 010-1234-1234"
    override val add: String = "추가"
    override val unknown: String = "무명"
    override val registrationNumber: String = "주민/사업자 번호"

}

object English : Locale {
    /** Home Menu **/
    override val statistics: String = "Statistics"
    override val donation: String = "Donation"
    override val search: String = "Search"
    override val report: String = "Report"
    override val addDonation: String = "Add Donation"
    /** Add Donation **/
    override val stepSearchContributor: String = "1. Search Contributor"
    override val stepAddProduct: String = "2. Add Product"
    override val stepConfirm: String = "3. Confirm"
    override val name: String = "Name"
    override val phoneNumber: String = "Phone Number"
    override val address: String = "Address"
    override val nameOrPhoneNumber: String = "Search Name or Phone number"
    override val hintSearch: String = "Gil-dong Hong or 010-1234-1234"
    override val add: String = "Add"
    override val unknown: String = "Unknown"
    override val registrationNumber: String = "Registration number"
}