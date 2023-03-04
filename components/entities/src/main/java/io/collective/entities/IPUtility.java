package io.collective.entities;

import com.google.common.net.InetAddresses;

import java.math.BigInteger;

public class IPUtility {
    public enum IPType {IPv4, IPv6, IPv4Private, Neither}

    /**
     * Determine if this is a valid IPv4 IP address
     * @param ip the IP address to evaluate if it's IPv4
     * @return true if valid formed IPv4 else return false
     */
    public static boolean isValidIPv4(String ip) {
        String[] nums = ip.split("\\.", -1);
        for (String x : nums) {
            // Validate integer in range (0, 255):
            // 1. length of chunk is between 1 and 3
            if (x.length() == 0 || x.length() > 3) return false;
            // 2. no extra leading zeros
            if (x.charAt(0) == '0' && x.length() != 1) return false;
            // 3. only digits are allowed
            for (char ch : x.toCharArray()) {
                if (!Character.isDigit(ch)) return false;
            }
            // 4. less than 255
            if (Integer.parseInt(x) > 255) return false;
        }
        return true;
    }

    public static boolean isValidIPv6(String ip) {
        String[] nums = ip.split(":", -1);
        String hexdigits = "0123456789abcdefABCDEF";
        for (String x : nums) {
            // Validate hexadecimal in range (0, 2**16):
            // 1. at least one and not more than 4 hexdigits in one chunk
            if (x.length() == 0 || x.length() > 4) return false;
            // 2. only hexdigits are allowed: 0-9, a-f, A-F
            for (Character ch : x.toCharArray()) {
                if (hexdigits.indexOf(ch) == -1) return false;
            }
        }
        return true;
    }

    /**
     * <a href="https://www.arin.net/reference/research/statistics/address_filters/">
     *     IPv4 Private Address Space and Filtering</a>
     * 10.0.0.0/8 IP addresses: 10.0.0.0 – 10.255.255.255
     * 172.16.0.0/12 IP addresses: 172.16.0.0 – 172.31.255.255
     * 192.168.0.0/16 IP addresses: 192.168.0.0 – 192.168.255.255
     *
     * @param ip The IP address represented as a String.  It's expected that the caller already validated
     *           that this is a validly formatted IPv4 IP address (see above isValidIPv4 method)
     * @return true if this is a private IPv4 IP address.  Else return false.
     */
    private static boolean isPrivateIPv4(String ip) {
        String[] nums = ip.split("\\.", -1);
        String first = nums[0];
        switch (first) {
            case "10":
                return true;
            case "172":
                    int num = Integer.parseInt(nums[1]);
                    if(num >= 16 && num <= 31)
                        return true;
                break;
            case "192":
                if(Integer.parseInt(nums[1]) == 168) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static IPType getIpAddressType(String ip) {
        if (ip.chars().filter(ch -> ch == '.').count() == 3 && isValidIPv4(ip)) {
            if(isPrivateIPv4(ip))
                return IPType.IPv4Private;
            else
                return IPType.IPv4;
        } else if (ip.chars().filter(ch -> ch == ':').count() == 7 && isValidIPv6(ip)) {
            return IPType.IPv6;
        } else return IPType.Neither;
    }

//    private static long ipToLong(String ipAddress) {
//        long result = 0;
//        String[] atoms = ipAddress.split("\\.");
//
//        for (int i = 3; i >= 0; i--) {
//            result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
//        }
//
//        return result;
//    }

//    public static BigInteger convertIPtoBigInteger(String ipString) throws UnknownHostException {
//        InetAddress ia = java.net.InetAddress.getByName(ipString);
//        byte[] byteArr = ia.getAddress();
//
//        if (ia instanceof java.net.Inet6Address) {
//            return new BigInteger(1, byteArr);
//        } else if (ia instanceof java.net.Inet4Address) {
//            return  BigInteger.valueOf(ipToLong(ipString));
//        }
//        return null;
//    }
//}
public static BigInteger convertIPtoBigInteger(String ipString) {
        return InetAddresses.toBigInteger(InetAddresses.forString(ipString));
    }
}
