package com.acc.goodwill.data.source.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@OptIn(ExperimentalUnsignedTypes::class)
object ProductTable : LongIdTable() {
    val contributorId: Column<Long> = long("contributor_id").index()
    val donationId: Column<Long> = long("donation_id").index()
    val category = integer("category").index()
    val label = text("label")
    val total = integer("total")
    val error = integer("error")
    val correct = integer("correct")
    val price = ulong("price")
    val transferPrice = ulong("transfer_price")
    val createdAt: Column<LocalDateTime> = datetime("created_at")
}