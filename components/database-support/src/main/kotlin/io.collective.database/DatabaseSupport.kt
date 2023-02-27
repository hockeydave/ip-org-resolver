package io.collective.database

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun createDatasource(
    jdbcUrl: String, username: String, password: String,
): DataSource = HikariDataSource().apply {
    setJdbcUrl(jdbcUrl)
    setUsername(username)
    setPassword(password)
    10.also { maximumPoolSize = it }
}

fun shutdownDataSource(dataSource: HikariDataSource) {
    dataSource.close()
}
