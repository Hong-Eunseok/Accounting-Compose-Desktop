package com.acc.goodwill.data.source.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object DonationTable : LongIdTable() {
    val contributorId: Column<Long> = long("contributor_id").index()
    val total: Column<Int> = integer("total")
    val total_error = integer("total_error")
    val total_correct = integer("total_correct")
    @OptIn(ExperimentalUnsignedTypes::class)
    val price = ulong("price")
    val fromType = integer("from_type")
    val organization = integer("organization")
    val member = bool("member")
    val createdAt = datetime("created_at").index().default(LocalDateTime.now())
}