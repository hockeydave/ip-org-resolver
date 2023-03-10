package test.collective.iporgs

import io.collective.database.DatabaseTemplate
import io.collective.entities.IPUtility
import io.collective.entities.Org
import io.collective.entities.OrgIPDataGateway
import io.collective.entities.OrgType
import io.collective.testsupport.testDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrgIPDataGatewayTest {
    private val dataSource = testDataSource()
    // T-mobile blocks
    private val ipStartBlock1 = "100.128.0.0"
    private val ipEndBlock1 = "100.128.0.9"
    private val ipMidRange1 = "100.128.0.4"
    private val ipStartBlock2 = "162.160.0.0"
    private val ipEndBlock2 = "162.160.0.11"
    //private val ipMidRange2 = "162.160.0.6"
    private val tMobileOrgId: Long = 202
    private val charterCommunicationsOrgId: Long  = 200
    private val verizonBusinessOrgId: Long  = 201
    private var testOrg: Org? = null

    @Before
    fun before() {
        clean()
        DatabaseTemplate(dataSource).apply {
            execute("INSERT INTO org(id, name, org_type_id) values (200, 'Charter Communications Inc',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (201, 'Verizon Business (MCICS)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            execute("INSERT INTO org(id, name, org_type_id) values (202, 'T-Mobile USA, Inc. (TMOBI)',"
                    + OrgType.RESIDENTIAL_ISP.ordinal + ");")
            val s1 = String.format("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (1, '%d', '%d', 202)",
                IPUtility.convertIPtoBigInteger(ipStartBlock1), IPUtility.convertIPtoBigInteger(ipEndBlock1))
            execute(s1)
            val s2 = String.format("insert into org_ip(id, start_block_ip, end_block_ip, org_id) values (2, '%d', '%d', 202)",
                IPUtility.convertIPtoBigInteger(ipStartBlock2), IPUtility.convertIPtoBigInteger(ipEndBlock2))
            execute(s2)
        }
    }
    @After
    fun after() {
        clean()
    }
    private fun clean() {
        val s = String.format("delete from org where id = %d", testOrg?.id ?: charterCommunicationsOrgId)
        DatabaseTemplate(dataSource).apply {
            execute("delete from org_ip where org_id in (200,201, 202)")
            execute("delete from org where id in (200,201, 202)")
            execute(s)
        }
    }

    @Test
    fun createIpOrg() {
        val gateway = OrgIPDataGateway(dataSource)
        val startIp = IPUtility.convertIPtoBigInteger("100.0.0.0")
        val endIp = IPUtility.convertIPtoBigInteger("100.19.255.255")
        val orgIpRecord = gateway.createOrgIp(3, startIp, endIp, tMobileOrgId)
        assertTrue(orgIpRecord.id > 0)
        assertEquals(startIp, orgIpRecord.startIP)
        assertEquals(endIp, orgIpRecord.endIP)
    }

    @Test
    fun createOrg() {
        val gateway = OrgIPDataGateway(dataSource)
        testOrg = gateway.createOrg("AT&T Mobility LLC -OrgIPDataGatewayTest", OrgType.RESIDENTIAL_ISP.ordinal)
        assertTrue(testOrg!!.id > 0)
        assertEquals("AT&T Mobility LLC -OrgIPDataGatewayTest", testOrg!!.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, testOrg!!.orgType)
    }

    @Test
    fun selectAll() {
        val gateway = OrgIPDataGateway(dataSource)
        var orgIpRecords = gateway.findAllByOrg(tMobileOrgId)
        assertEquals(2, orgIpRecords.size)
        orgIpRecords = gateway.findAllByOrg(verizonBusinessOrgId)
        assertEquals(0, orgIpRecords.size)
        orgIpRecords = gateway.findAllByOrg(charterCommunicationsOrgId)
        assertEquals(0, orgIpRecords.size)
    }

    @Test
    fun findById() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findById(charterCommunicationsOrgId)!!
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByIp() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findByIp(ipMidRange1)!!
        assertEquals("T-Mobile USA, Inc. (TMOBI)", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun findByName() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findByName("Charter Communications Inc")!!
        assertEquals(charterCommunicationsOrgId, org.id)
        assertEquals("Charter Communications Inc", org.name)
        assertEquals(OrgType.RESIDENTIAL_ISP.ordinal, org.orgType)
    }

    @Test
    fun update() {
        val gateway = OrgIPDataGateway(dataSource)
        val org = gateway.findById(tMobileOrgId)!!

        org.orgType = OrgType.CLOUD.ordinal
        org.name = "Verizon Data Services LLC (VERIZ-557-Z)"
        gateway.update(org)

        val updated = gateway.findById(tMobileOrgId)!!
        assertEquals("Verizon Data Services LLC (VERIZ-557-Z)", updated.name)
        assertEquals(OrgType.CLOUD.ordinal, updated.orgType)
    }

}