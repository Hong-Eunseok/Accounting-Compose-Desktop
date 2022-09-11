package com.acc.goodwill.data.source.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ContributorTable : LongIdTable() {
    val name: Column<String> = text("name").index()
    val phoneNumber: Column<String?> = text("phone_number").nullable().index()
    val address: Column<String?> = text("address").nullable()
    val registrationNumber: Column<String?> = text("registration_number").nullable()
    val recommend: Column<Int> = integer("recommend").default(0)
    val registrationType: Column<Int> = integer("registration_type").default(0)
    val createdAt: Column<LocalDateTime> = datetime("created_at").index().default(LocalDateTime.now())
    val updatedAt: Column<LocalDateTime> = datetime("updated_at").default(LocalDateTime.now())
}