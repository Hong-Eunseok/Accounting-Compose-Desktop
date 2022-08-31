package com.acc.goodwill.data.source

import com.acc.goodwill.data.source.table.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class UserDao @Inject constructor() {

    fun query() {
        transaction {
            val query = User.select { User.id eq 1 }
            query.distinct
            query.forEach {
                println(it[User.name])
            }
        }
    }

}