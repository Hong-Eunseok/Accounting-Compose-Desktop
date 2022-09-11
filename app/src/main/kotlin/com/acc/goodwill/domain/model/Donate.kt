package com.acc.goodwill.domain.model

import com.acc.goodwill.data.source.table.DonationTable
import org.jetbrains.exposed.dao.id.EntityID


data class Donate(
    val contributor: Contributor?,
    val donate: Donation,
    val primaryKey: EntityID<Long> = EntityID(-1, DonationTable),
) {
    data class Donation(val total: Int, val error: Int, val correct: Int, val price: ULong)

    companion object {
        val INIT: Donate = Donate(
            null, Donation(0, 0, 0, 0u)
        )
    }
}