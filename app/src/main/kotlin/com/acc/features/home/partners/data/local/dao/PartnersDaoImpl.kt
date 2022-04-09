package com.acc.features.home.partners.data.local.dao

import com.acc.features.home.partners.model.Partner
import com.utils.UuidUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import java.sql.Connection

class PartnersDaoImpl(
    private val connection: Connection,
    private val uuidUtils: UuidUtils
) : PartnersDao {

    private val table = "partner"

    init {
        createTableIfMissing()
    }

    private fun createTableIfMissing() {
        val createTableStatement =
            """
            CREATE TABLE IF NOT EXISTS $table (
            id text PRIMARY KEY,
            name text NOT NULL,
            address text NOT NULL,
            phone_number text NOT NULL
            )
            """
        val statement = connection.createStatement()
        statement.execute(createTableStatement)
        statement.close()
    }

    private val updatePartners: MutableSharedFlow<Unit> = MutableSharedFlow<Unit>(1).also {
        it.tryEmit(Unit)
    }

    override suspend fun insertPartner(name: String, address: String, phoneNumber: String) {
        val insertPartnerStatement = "INSERT INTO $table values(?,?,?,?)"
        val prepareStatement = connection.prepareStatement(insertPartnerStatement)
        prepareStatement.setString(1, uuidUtils.getUuid())
        prepareStatement.setString(2, name)
        prepareStatement.setString(3, address)
        prepareStatement.setString(4, phoneNumber)
        prepareStatement.executeUpdate()
        updatePartners.emit(Unit)
    }

    override fun getPartners(): Flow<List<Partner>> {
        return updatePartners.map {
            val query = "SELECT * FROM $table"
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)
            val chartOfAccounts = buildList {
                while (resultSet.next()) {
                    add(
                        with(resultSet) {
                            Partner(
                                id = getString("id"),
                                name = getString("name"),
                                address = getString("address"),
                                phoneNumber = getString("phone_number")
                            )
                        }
                    )
                }
                resultSet.close()
                statement.close()
            }
            chartOfAccounts
        }
    }
}