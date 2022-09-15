package com.acc.goodwill.domain.model

import com.acc.goodwill.data.source.table.DonationTable
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime


data class Donate(
    val donateId: EntityID<Long>,
    val contributor: Contributor?,
    val donate: Donation,
    val primaryKey: EntityID<Long> = EntityID(-1, DonationTable),
) {
    data class Donation(
        val total: Int,
        val error: Int,
        val correct: Int,
        val price: ULong,
        val createAt: LocalDateTime,
        val optionParsingData: String?,
        val organization: Int,
        val member: Boolean,
        val fromType: Int,
    )

    companion object {
        val INIT: Donate = Donate(
            EntityID(0, DonationTable),
            null,
            Donation(0, 0, 0, 0u, LocalDateTime.now(), null, 0, false, 0)
        )

        val FROM_TYPE = listOf("기증", "수거", "기타")
        val ORGANIZATION = listOf("개인", "단체")
    }
}