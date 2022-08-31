package com.acc.goodwill.data.source.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object User : IntIdTable() {
    val name: Column<String> = text("name").index()
    val phoneNumber: Column<String> = text("phone_number").index()
    val address: Column<String?> = text("address").nullable()
    val registrationNumber: Column<String?> = text("registration_number").nullable()
}