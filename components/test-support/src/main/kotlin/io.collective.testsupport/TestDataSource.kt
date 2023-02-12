package io.collective.testsupport

import io.collective.database.createDatasource
import javax.sql.DataSource

const val testJdbcUrl = "jdbc:postgresql://localhost:5432/ip_test"
const val testDbUsername = "dcp"
const val testDbPassword = "dcp"

fun testDataSource(): DataSource {
    return createDatasource(
        jdbcUrl = testJdbcUrl,
        username = testDbUsername,
        password = testDbPassword
    )
}
