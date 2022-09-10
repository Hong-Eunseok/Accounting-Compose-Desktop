package com.acc.goodwill.data.source.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object ContributorTable : LongIdTable() {
    val name: Column<String> = text("name").index()
    val phoneNumber: Column<String?> = text("phone_number").nullable().index()
    val address: Column<String?> = text("address").nullable()
    val registrationNumber: Column<String?> = text("registration_number").nullable()
    val recommend: Column<Int> = integer("recommend").default(0)
}