package io.collective.entities

import io.collective.database.DatabaseTemplate
import org.slf4j.LoggerFactory
import java.math.BigInteger
import javax.sql.DataSource

class OrgIPDataGateway(private val dataSource: DataSource) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val template = DatabaseTemplate(dataSource)

    private fun createOrgIp(startIP: BigInteger, endIP: BigInteger, orgId: Int): OrgIPRecord {
        return template.create(
            "insert into org_ip (start_block_ip, end_block_ip, org_id) values (?, ?, ?)", { id ->
                OrgIPRecord(id, startIP, endIP, orgId)
            }, startIP, endIP, orgId
        )
    }
    fun createOrgIp(pkey: Long, startIP: BigInteger, endIP: BigInteger, orgId: Int): OrgIPRecord {
        return template.create(
            "insert into org_ip (id, start_block_ip, end_block_ip, org_id) values (?, ?, ?, ?)", { id ->
                OrgIPRecord(id, startIP, endIP, orgId)
            }, pkey, startIP, endIP, orgId
        )
    }

    fun createOrg(name: String, typeId: Int): Org {
        return template.create(
            "insert into org (name, org_type_id) values (?, ?)", { id ->
                Org(id, name, typeId)
            }, name, typeId
        )
    }


    fun findAll(): List<OrgIPRecord> {
        return template.findAll("select id, start_block_ip, end_block_ip, org_id from org_ip order by start_block_ip asc") { rs ->
            OrgIPRecord(
                rs.getLong(1),
                rs.getBigDecimal(2).toBigInteger(),
                rs.getBigDecimal(3).toBigInteger(),
                rs.getInt(4)
            )
        }
    }

//    fun findBy(id: Long): OrgIPRecord? {
//        return template.findBy(
//            "select id, name, quantity from org_ip where id = ?", { rs ->
//                OrgIPRecord(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4))
//            }, id
//        )
//    }

    fun findById(id: Long): Org? {
        return template.findById(
            "select id, name, org_type_id from org where id = ?", { rs ->
                Org(rs.getLong(1), rs.getString(2), rs.getInt(3))
            }, id
        )
    }

    fun findByIp(ipAddress: String): Org? {
        val ip = IPUtility.convertIPtoBigInteger(ipAddress)
        val res = template.findByIP(
            "SELECT org.id, org.name, org.org_type_id FROM org LEFT JOIN org_ip ON org.id = org_ip.org_id where start_block_ip <= ? and end_block_ip >= ?",
            { rs ->
                Org(rs.getLong(1), rs.getString(2), rs.getInt(3))
            }, ip
        )
        return res
    }

    fun findByName(name: String): Org? {
        val res = template.findByName(
            "SELECT org.id, org.name, org.org_type_id FROM org where org.name=?",
            { rs ->
                Org(rs.getLong(1), rs.getString(2), rs.getInt(3))
            }, name
        )
        return res
    }


    fun update(org: Org): Org {
        template.update(
            "update org set name = ?, org_type_id = ? where id = ?",
            org.name, org.orgType, org.id
        )
        return org
    }

    fun save(startIP: BigInteger, endIP: BigInteger, orgId: Int): OrgIPRecord {
        return createOrgIp(startIP, endIP, orgId)
    }

}