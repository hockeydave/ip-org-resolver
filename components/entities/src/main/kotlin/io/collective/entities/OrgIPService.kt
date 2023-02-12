package io.collective.entities

class OrgIPService(private val dataGateway: OrgIPDataGateway) {
    fun findAll(): List<OrgIPRecord> {
        return dataGateway.findAll().map { OrgIPRecord(it.id, it.startIP, it.endIP, it.orgId) }
    }

    fun findBy(id: Long): Org {
        val record = dataGateway.findBy(id)!!
        return Org(record.id, record.name, record.orgType)
    }

    fun findBy(ip: String): OrgIPRecord {
        val record = dataGateway.findBy(ip)!!
        return OrgIPRecord(record.id, record.startIP, record.endIP, record.orgId)
    }

    fun update(org: Org): Org {
        val record = dataGateway.findBy(org.id)!!
        record.orgType = org.orgType
        record.name = org.name
        dataGateway.update(record)
        return findBy(record.id)
    }

}