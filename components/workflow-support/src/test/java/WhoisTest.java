import io.collective.ip.WhoIs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WhoisTest {
    WhoIs whois;
    @Before
    public void setUp() throws Exception {
        whois = new WhoIs();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGoodIpv4Lookup() {
        String result = whois.getOrg("100.8.12.180");
        assertEquals("Verizon Business (MCICS)", result);
    }
    @Test
    public void testGoodIpv6Lookup() {
        String result = whois.getOrg("2600:1010:0000:0000:0000:0000:3257:9652");
        assertEquals("Verizon Business (MCICS)", result);
    }

    @Test
    public void testBadIpv4Lookup() {
        String result = whois.getOrg("256.1.1.1");
        assertNull(result);
    }

    @Test
    public void testBadIpv6Lookup() {
        String result = whois.getOrg("2001:0db8:85a3:0000:0000:8a2e:0370");
        assertNull(result);
    }
}
