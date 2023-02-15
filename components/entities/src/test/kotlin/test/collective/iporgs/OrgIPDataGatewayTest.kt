package test.collective.iporgs

import io.collective.database.DatabaseTemplate
import io.collective.entities.OrgIPDataGateway
import io.collective.entities.OrgType
import io.collective.testsupport.testDataSource
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrgIPDataGatewayTest {
    private val dataSource = testDataSource()

    @Before
    fun before() {
        DatabaseTemplate(dataSource).apply {
            execute("delete from org_ip")
            execute("delete from org")
            execute("INSERT INTO org(id, name, org_type_id) values (1, 'Charter Communications Inc',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (2, 'Verizon Business (MCICS)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (3, 'T-Mobile USA, Inc. (TMOBI)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (1, '98.24.0.0', '98.31.255.255', 1)")
            execute("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (2, '98.6.0.0', '98.6.255.255', 1)")
        }
    }

    @Test
    fun createOrg() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.createOrg("AT&T Mobility LLC", OrgType.RESIDENTIAL_ISP.ordinal)
        assertTrue(org.id > 0)
        assertEquals("AT&T Mobility LLC", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun selectAll() {
        val gateway = OrgIPDataGateway(dataSource)
        val orgs = gateway.findAll()
        assertEquals(2, orgs.size)
    }

    @Test
    fun findById() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findById(1)!!
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByIp() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findByIp("98.24.0.0")!!
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByName() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findByName("Charter Communications Inc")!!
        assertEquals(1, org.id)
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun update() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findById(3)!!

        org.orgType = OrgType.CLOUD.ordinal
        org.name = "Verizon Data Services LLC (VERIZ-557-Z)"
        gateway.update(org)

        val updated = gateway.findById(3)!!
        assertEquals("Verizon Data Services LLC (VERIZ-557-Z)", updated.name)
        assertEquals(OrgType.CLOUD.ordinal, updated.orgType)
    }

}