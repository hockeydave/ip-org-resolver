package io.collective.database

import javax.sql.DataSource

const val devJdbcUrl = "jdbc:postgresql://localhost:5432/ip_development"
const val devDbUsername = "dcp"
const val devDbPassword = "dcp"

fun devDataSource(): DataSource {
    return createDatasource(
        jdbcUrl = devJdbcUrl,
        username = devDbUsername,
        password = devDbPassword
    )
}
