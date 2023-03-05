package test.collective.iporgs;

import io.collective.entities.IPUtility;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IPUtilityTest {


    @Test
    public void isValidIPv4() {
        assertTrue(IPUtility.isValidIPv4("200.100.100.100"));
    }

    @Test
    public void isValidIPv6() {
        assertTrue(IPUtility.isValidIPv6("0000:0000:0000:0000:0000:ffff:c0a8:0001"));
    }

    @Test
    public void getIpAddressType() {
        assertEquals(IPUtility.IPType.IPv4, IPUtility.getIpAddressType("100.100.100.100"));
        assertEquals(IPUtility.IPType.IPv6, IPUtility.getIpAddressType("0000:0000:0000:0000:0000:ffff:c0a8:0001"));
        assertEquals(IPUtility.IPType.IPv4Private, IPUtility.getIpAddressType("192.168.0.1"));
        assertEquals(IPUtility.IPType.Neither, IPUtility.getIpAddressType("300.300.300.300"));
    }

    @Test
    public void convertIPv4toBigInteger()  {
        assertEquals(BigInteger.valueOf(3232235521L), IPUtility.convertIPtoBigInteger("192.168.0.1"));

    }

    @Test
    public void convertIPv6toBigInteger()  {
        assertEquals(BigInteger.valueOf(3232235521L), IPUtility.convertIPtoBigInteger("::ffff:c0a8:1"));
        assertEquals(new BigInteger("42541956123769884636017138956568135816"), IPUtility.convertIPtoBigInteger("2001:4860:4860::8888"));
    }
}