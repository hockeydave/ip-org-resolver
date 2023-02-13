import io.collective.entities.Org;
import io.collective.ip.WhoIs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WhoisTest {
    WhoIs whois;
    @Before
    public void setUp()  {
        whois = new WhoIs();
    }

    @After
    public void tearDown()  {

    }

    @Test
    public void testGoodIpv4Lookup() {
        Org org = whois.getOrg("100.8.12.180");
        assertEquals("Verizon Business (MCICS)", org.getName());
    }
    @Test
    public void testGoodIpv6Lookup() {
        Org org = whois.getOrg("2600:1010:0000:0000:0000:0000:3257:9652");
        assertEquals("Verizon Business (MCICS)", org.getName());
    }

    @Test
    public void testBadIpv4Lookup() {
        Org org = whois.getOrg("256.1.1.1");
        assertNull(org);
    }

    @Test
    public void testBadIpv6Lookup() {
        Org org = whois.getOrg("2001:0db8:85a3:0000:0000:8a2e:0370");
        assertNull(org);
    }
}
