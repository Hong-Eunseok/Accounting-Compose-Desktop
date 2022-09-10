package com.acc.goodwill.domain.model

import com.acc.goodwill.data.source.table.ContributorTable
import org.jetbrains.exposed.dao.id.EntityID

data class Contributor(
    val name: String,
    val primaryKey: EntityID<Long> = EntityID(-1, ContributorTable),
    val phoneNumber: String? = null,
    val address: String? = null,
    val registrationNumber: String? = null,
    val recommend: Int = 0,
) {

    fun validUser(): Boolean {
        return primaryKey.value >= -1
    }
    companion object {
        val UNKNOWN: Contributor = Contributor("무명", phoneNumber = "")
        val INIT: Contributor = Contributor("초기화", EntityID(-2, ContributorTable))
    }
}