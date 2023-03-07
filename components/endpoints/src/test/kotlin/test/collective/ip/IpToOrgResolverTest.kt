package test.collective.ip

import io.collective.ip.IpToOrgResolver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class IpToOrgResolverTest {
    private val ipToOrgResolver = IpToOrgResolver()
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testGoodIpv4Lookup() {
        val org = ipToOrgResolver.getOrg("100.8.12.180")
        assertEquals("Verizon Business (MCICS)", org.name)
    }

    @Test
    fun testGoodIpv6Lookup() {
        val org = ipToOrgResolver.getOrg("2600:1010:0000:0000:0000:0000:3257:9652")
        assertEquals("Verizon Business (MCICS)", org.name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBadIpv4Lookup() {
        val org = ipToOrgResolver.getOrg("256.1.1.1")
        assertNull(org)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBadIpv6Lookup() {
        val org = ipToOrgResolver.getOrg("2001:0db8:85a3:0000:0000:8a2e:0370")
        assertNull(org)
    }

    @Test
    fun testIpWoRef() {
        val org = ipToOrgResolver.getOrg("200.200.200.200")
        assertEquals("Latin American and Caribbean IP address Regional Registry (LACNIC)", org.name)
    }
}