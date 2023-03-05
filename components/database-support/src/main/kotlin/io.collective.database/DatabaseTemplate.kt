package io.collective.database


import java.math.BigDecimal
import java.math.BigInteger
import java.sql.*
import java.time.LocalDate
import javax.sql.DataSource

class DatabaseTemplate(private val dataSource: DataSource) {

    fun <T> create(sql: String, id: (Long) -> T, vararg params: Any) =
            dataSource.connection.use { connection ->
                create(connection, sql, id, *params)
            }

    private fun <T> create(connection: Connection, sql: String, id: (Long) -> T, vararg params: Any): T {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
            setParameters(params, statement)
            statement.executeUpdate()
            val keys = statement.generatedKeys
            keys.next()
            id(keys.getLong(1))
        }
    }

    fun <T> findAll(sql: String, mapper: (ResultSet) -> T): List<T> {
        dataSource.connection.use { connection ->
            return findAll(connection, sql, mapper)
        }
    }

    private fun <T> findAll(connection: Connection, sql: String, mapper: (ResultSet) -> T): List<T> {
        return query(connection, sql, {}, mapper)
    }

    fun <T> findAllByOrg(sql: String, mapper: (ResultSet) -> T, orgId: Long): List<T> {
        dataSource.connection.use { connection ->
            return findAllByOrg(connection, sql, mapper, orgId)
        }
    }

    private fun <T> findAllByOrg(connection: Connection, sql: String, mapper: (ResultSet) -> T, orgId: Long): List<T> {
        return query(connection, sql, {ps -> ps.setLong(1, orgId)}, mapper)
    }

    fun <T> findById(sql: String, mapper: (ResultSet) -> T, id: Long): T? {
        dataSource.connection.use { connection ->
            return findById(connection, sql, mapper, id)
        }
    }

    private fun <T> findById(connection: Connection, sql: String, mapper: (ResultSet) -> T, id: Long): T? {
        val list = query(connection, sql, { ps -> ps.setLong(1, id) }, mapper)
        return list.firstOrNull()
    }

    fun <T> findByIP(sql: String, mapper: (ResultSet) -> T, ipAddress: BigInteger): T? {
        dataSource.connection.use { connection ->
            return findByIP(connection, sql, mapper, ipAddress)
        }
    }

    private fun <T> findByIP(connection: Connection, sql: String, mapper: (ResultSet) -> T, ipAddress: BigInteger): T? {
        val ip = BigDecimal(ipAddress)
        val list = query(connection, sql, { ps -> ps.setBigDecimal(1, ip)
            ps.setBigDecimal(2, ip) }, mapper)
        return list.firstOrNull()
    }

    fun <T> findByName(sql: String, mapper: (ResultSet) -> T, name: String): T? {
        dataSource.connection.use { connection ->
            return findByName(connection, sql, mapper, name)
        }
    }

    private fun <T> findByName(connection: Connection, sql: String, mapper: (ResultSet) -> T, name: String): T? {
        val list = query(connection, sql, { ps -> ps.setString(1, name) }, mapper)
        return list.firstOrNull()
    }

    fun update(sql: String, vararg params: Any) {
        dataSource.connection.use { connection ->
            update(connection, sql, *params)
        }
    }

    private fun update(connection: Connection, sql: String, vararg params: Any) {
        return connection.prepareStatement(sql).use { statement ->
            setParameters(params, statement)
            statement.executeUpdate()
        }
    }

    fun <T> query(sql: String, params: (PreparedStatement) -> Unit, mapper: (ResultSet) -> T): List<T> {
        dataSource.connection.use { connection ->
            return query(connection, sql, params, mapper)
        }
    }

    private fun <T> query(
            connection: Connection,
            sql: String,
            params: (PreparedStatement) -> Unit,
            mapper: (ResultSet) -> T
    ): List<T> {
        val results = ArrayList<T>()
        connection.prepareStatement(sql).use { statement ->
            params(statement)
            statement.executeQuery().use { rs ->
                while (rs.next()) {
                    results.add(mapper(rs))
                }
            }
        }
        return results
    }

    private fun setParameters(params: Array<out Any>, statement: PreparedStatement) {
        for (i in params.indices) {
            val param = params[i]
            val parameterIndex = i + 1

            when (param) {
                is String -> statement.setString(parameterIndex, param)
                is Int -> statement.setInt(parameterIndex, param)
                is Long -> statement.setLong(parameterIndex, param)
                is Boolean -> statement.setBoolean(parameterIndex, param)
                is LocalDate -> statement.setDate(parameterIndex, Date.valueOf(param))
                is BigInteger -> statement.setBigDecimal(parameterIndex, BigDecimal(param))
                is BigDecimal -> statement.setBigDecimal(parameterIndex, param)
            }
        }
    }

    /// USED FOR TESTING

    fun execute(sql: String) {
        dataSource.connection.use { connection ->
            connection.prepareCall(sql).use(CallableStatement::execute)
        }
    }
}