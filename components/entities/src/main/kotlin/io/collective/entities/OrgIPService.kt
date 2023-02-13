package io.collective.entities

class OrgIPService(private val dataGateway: OrgIPDataGateway) {

    fun create(name: String, typeId: Int): Org {
        return dataGateway.create(name, typeId)
    }

    fun create(startIP: String, endIP: String, orgId: Int): OrgIPRecord {
        return dataGateway.create(startIP, endIP, orgId)
    }

    fun findAll(): List<OrgIPRecord> {
        return dataGateway.findAll().map { OrgIPRecord(it.id, it.startIP, it.endIP, it.orgId) }
    }

    fun findById(id: Long): Org {
        val record = dataGateway.findById(id)!!
        return Org(record.id, record.name, record.orgType)
    }

    fun findByIp(ip: String): Org? {
        val record = dataGateway.findByIp(ip)
        return if(record == null) null else {
            Org(record.id, record.name, record.orgType)
        }
    }

    fun findByName(name: String): Org? {
        val record = dataGateway.findByName(name)
        return if(record == null) null else {
            Org(record.id, record.name, record.orgType)
        }
    }

    fun update(org: Org): Org {
        val record = dataGateway.findById(org.id)!!
        record.orgType = org.orgType
        record.name = org.name
        dataGateway.update(record)
        return findById(record.id)
    }

}