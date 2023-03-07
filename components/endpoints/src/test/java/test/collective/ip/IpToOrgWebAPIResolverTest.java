package test.collective.ip;

import io.collective.entities.FullOrg;
import io.collective.ip.IpToOrgWebAPIResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IpToOrgWebAPIResolverTest {
    IpToOrgWebAPIResolver ipToOrgWebAPIResolver;
    @Before
    public void setUp()  {
        ipToOrgWebAPIResolver = new IpToOrgWebAPIResolver();
    }

    @After
    public void tearDown()  {

    }

    @Test
    public void testGoodIpv4Lookup() {
        FullOrg fullOrg = ipToOrgWebAPIResolver.getOrg("100.8.12.180");
        assertEquals("Verizon Business (MCICS)", fullOrg.getOrg().getName());
        assertEquals("https://rdap.arin.net/registry/entity/MCICS", fullOrg.getUrl());
    }
    @Test
    public void testGoodIpv6Lookup() {
        FullOrg fullOrg = ipToOrgWebAPIResolver.getOrg("2600:1010:0000:0000:0000:0000:3257:9652");
        assertEquals("Verizon Business (MCICS)", fullOrg.getOrg().getName());
        assertEquals("https://rdap.arin.net/registry/entity/MCICS", fullOrg.getUrl());
    }

    @Test
    public void testBadIpv4Lookup() {
        FullOrg fullOrg = ipToOrgWebAPIResolver.getOrg("256.1.1.1");
        assertNull(fullOrg);
    }

    @Test
    public void testBadIpv6Lookup() {
        FullOrg fullOrg = ipToOrgWebAPIResolver.getOrg("2001:0db8:85a3:0000:0000:8a2e:0370");
        assertNull(fullOrg);
    }

    @Test
    public void testIpWoRef() {
        FullOrg fullOrg = ipToOrgWebAPIResolver.getOrg("200.200.200.200");
        assertEquals("Latin American and Caribbean IP address Regional Registry (LACNIC)", fullOrg.getOrg().getName());
        assertEquals("https://rdap.arin.net/registry/entity/LACNIC", fullOrg.getUrl());
    }
}
