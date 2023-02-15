package test.collective.iporgs

import io.collective.database.DatabaseTemplate
import io.collective.entities.*
import io.collective.testsupport.testDataSource
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OrgIPServiceTest {
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
    fun createIpOrg() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val orgIpRecord = service.createOrgIp(OrgIPRecord(3, "100.0.0.0", "100.19.255.255", 3))
        assertTrue(orgIpRecord.id > 0)
        assertEquals("100.0.0.0", orgIpRecord.startIP)
        assertEquals("100.19.255.255", orgIpRecord.endIP)
    }

    @Test
    fun createOrg() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.createOrg("AT&T Mobility LLC", OrgType.RESIDENTIAL_ISP.ordinal)
        assertTrue(org.id > 0)
        assertEquals("AT&T Mobility LLC", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }
    @Test
    fun findAll() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val orgIpRecords = service.findAll()
        assertEquals(2, orgIpRecords.size)
    }

    @Test
    fun findById() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findById(1)
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByIp() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findByIp("98.25.1.100")
        assertEquals(1, org?.id)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org?.orgType)
    }

    @Test   //(expected = NullPointerException::class)
    fun findByIpFail() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findByIp("22.25.1.100")
        assertNull(org)
    }

    @Test
    fun findByName() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findByName("T-Mobile USA, Inc. (TMOBI)")
        assertEquals(3, org?.id)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org?.orgType)
    }

    @Test
    fun update() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        service.update(Org(3, "Verizon Data Services LLC (VERIZ-557-Z)", OrgType.MOBILE_ISP.ordinal))

        val org = service.findById(3)
        assertEquals("Verizon Data Services LLC (VERIZ-557-Z)", org.name)
        assertEquals(OrgType.MOBILE_ISP.ordinal, org.orgType)
    }

}