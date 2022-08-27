package com.acc.features.organization.data.local.dao

import com.acc.features.organization.model.Organization
import com.database.DatabaseConnection
import com.utils.UuidUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrganizationDaoImpl @Inject constructor(
    private val db: DatabaseConnection,
) : OrganizationDao {

    private val table = "organization"

    private val uuidUtils: UuidUtils = UuidUtils()

    init {
        createTableIfMissing()
    }

    private fun createTableIfMissing() {
        val createTableStatement =
            """
            CREATE TABLE IF NOT EXISTS $table (
            id text PRIMARY KEY,
            organizationId text NOT NULL,
            name text NOT NULL,
            postCode text NOT NULL,
            address text NOT NULL,
            selected integer NOT NULL,
            created_on integer NOT NULL
            )
            """
        val statement = db.connection.createStatement()
        statement.execute(createTableStatement)
    }

    private val updateOrganizations: MutableSharedFlow<Unit> = MutableSharedFlow<Unit>(1).also {
        it.tryEmit(Unit)
    }

    override suspend fun insertOrganization(organization: Organization) {
        val insertOrganizationStatement = "INSERT INTO $table values(?,?,?,?,?,?,?)"
        val prepareStatement = db.connection.prepareStatement(insertOrganizationStatement)
        prepareStatement.setString(1, uuidUtils.getUuid())
        prepareStatement.setString(2, organization.organizationId)
        prepareStatement.setString(3, organization.name)
        prepareStatement.setString(4, organization.postCode)
        prepareStatement.setString(5, organization.address)
        prepareStatement.setBoolean(6, organization.selected)
        prepareStatement.setLong(7, organization.createdOn)
        prepareStatement.executeUpdate()
        updateOrganizations.emit(Unit)
    }

    override fun getOrganizations(): Flow<List<Organization>> {
        return updateOrganizations
            .map {
                val query = "SELECT * FROM $table"
                val statement = db.connection.createStatement()
                val resultSet = statement.executeQuery(query)
                val organizationList = buildList {
                    while (resultSet.next()) {
                        add(
                            with(resultSet) {
                                Organization(
                                    id = getString("id"),
                                    organizationId = getString("organizationId"),
                                    name = getString("name"),
                                    postCode = getString("postCode"),
                                    address = getString("address"),
                                    selected = getBoolean("selected"),
                                    createdOn = getLong("created_on")
                                )
                            }
                        )
                    }
                    resultSet.close()
                    statement.close()
                }
                organizationList
            }
    }

    override suspend fun unselectOrganizations() {
        val unselectOrganizationQuery = "UPDATE $table SET selected = 0"
        val statement = db.connection.createStatement()
        statement.execute(unselectOrganizationQuery)
        statement.close()
        updateOrganizations.emit(Unit)
    }

    override suspend fun selectOrganization(organizationId: String) {
        unselectOrganizations()
        val selectOrganizationQuery = "UPDATE $table SET selected = 1 WHERE organizationId = ?"
        val preparedStatement = db.connection.prepareStatement(selectOrganizationQuery)
        preparedStatement.setString(1, organizationId)
        preparedStatement.executeUpdate()
        preparedStatement.close()
        updateOrganizations.emit(Unit)
    }
}