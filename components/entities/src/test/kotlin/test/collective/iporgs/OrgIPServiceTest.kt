package test.collective.iporgs

import io.collective.database.DatabaseTemplate
import io.collective.entities.*
import io.collective.testsupport.shutdownDataSource
import io.collective.testsupport.testDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OrgIPServiceTest {
    private val dataSource = testDataSource()
    private var testOrg: Org? = null
    private val tMobileOrgId: Long = 102
    private val charterCommunicationsOrgId: Long  = 100
    private val verizonBusinessOrgId: Long  = 101

    @Before
    fun before() {
        clean()
        DatabaseTemplate(dataSource).apply {
            execute("INSERT INTO org(id, name, org_type_id) values (100, 'Charter Communications Inc',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (101, 'Verizon Business (MCICS)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (102, 'T-Mobile USA, Inc. (TMOBI)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            val s1 = String.format("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (1, '%d', '%d', 100)",
                IPUtility.convertIPtoBigInteger("98.24.0.0"), IPUtility.convertIPtoBigInteger("98.31.255.255"))
            execute(s1)
            val s2 = String.format("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (2, '%d', '%d', 100)",
                IPUtility.convertIPtoBigInteger("98.6.0.0"), IPUtility.convertIPtoBigInteger("98.6.255.255"))
            execute(s2)
        }
    }
    @After
    fun after() {
        clean()
        shutdownDataSource(dataSource)
    }
    private fun clean() {
        val s = String.format("delete from org where id = %d", testOrg?.id ?: charterCommunicationsOrgId)
        DatabaseTemplate(dataSource).apply {
            execute("delete from org_ip where org_id in (100, 101, 102)")
            execute("delete from org where id in (100, 101, 102)")
            execute(s)
        }
    }

    @Test
    fun createIpOrg() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val startIp = IPUtility.convertIPtoBigInteger("100.0.0.0")
        val endIp = IPUtility.convertIPtoBigInteger("100.19.255.255")
        val orgIpRecord = service.createOrgIp(OrgIPRecord(3, startIp, endIp, tMobileOrgId))
        assertTrue(orgIpRecord.id > 0)
        assertEquals(startIp, orgIpRecord.startIP)
        assertEquals(endIp, orgIpRecord.endIP)
    }

    @Test
    fun createOrg() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        testOrg = service.createOrg("AT&T Mobility LLC - OrgIPServiceTest", OrgType.RESIDENTIAL_ISP.ordinal)
        assertTrue(testOrg!!.id > 0)
        assertEquals("AT&T Mobility LLC - OrgIPServiceTest", testOrg!!.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, testOrg!!.orgType)
    }
    @Test
    fun selectAll() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        var orgIpRecords = service.findAllByOrg(charterCommunicationsOrgId)
        assertEquals(2, orgIpRecords.size)
        orgIpRecords = service.findAllByOrg(verizonBusinessOrgId)
        assertEquals(0, orgIpRecords.size)
        orgIpRecords = service.findAllByOrg(tMobileOrgId)
        assertEquals(0, orgIpRecords.size)
    }

    @Test
    fun findById() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findById(charterCommunicationsOrgId)
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByIp() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findByIp("98.25.1.100")
        assertEquals(charterCommunicationsOrgId, org?.id)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org?.orgType)
    }

    @Test   //(expected = NullPointerException::class)
    fun findByIpFail() {
        try {
            val service = OrgIPService(OrgIPDataGateway(dataSource))
            val org = service.findByIp("22.25.1.100")
            assertNull(org)
        }catch (e: Exception) {
            e.printStackTrace()
            println("Failed findByIpFail:  $e")
        }

    }

    @Test
    fun findByName() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        val org = service.findByName("T-Mobile USA, Inc. (TMOBI)")
        assertEquals(tMobileOrgId, org?.id)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org?.orgType)
    }

    @Test
    fun update() {
        val service = OrgIPService(OrgIPDataGateway(dataSource))
        service.update(Org(tMobileOrgId, "Verizon Data Services LLC (VERIZ-557-Z)", OrgType.MOBILE_ISP.ordinal))

        val org = service.findById(tMobileOrgId)
        assertEquals("Verizon Data Services LLC (VERIZ-557-Z)", org.name)
        assertEquals(OrgType.MOBILE_ISP.ordinal, org.orgType)
    }

}