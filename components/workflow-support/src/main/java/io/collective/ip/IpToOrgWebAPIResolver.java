package io.collective.ip;

import io.collective.entities.Org;
import io.collective.entities.OrgType;
import org.apache.commons.net.finger.FingerClient;
import org.apache.commons.net.whois.WhoisClient;

import java.io.IOException;

public class IpToOrgWebAPIResolver extends FingerClient {
    WhoisClient whois;

    public IpToOrgWebAPIResolver() {
        whois = new WhoisClient();
    }


    /**
     * Get the domain information for the IP Address passed in and add the
     * Ref: <a href="https://rdap.arin.net/registry/ip/100.0.0.0">ARIN Registry</a>
     * to the queue for pulling all the IP ranges for this Organization.
     * whois 184.59.132.132 | grep '^Ref' | grep entity | awk '{print $2}' | xargs curl -s | jq '.networks[]
     * | {startAddress:.startAddress,endAddress:.endAddress}'
     * @param ipAddress  The IP Address to resolve
     * @return a String representation of the Organization Name that owns this IP address.
     */
    public  Org getOrg(String ipAddress) {
        try {
            //whois.connect(WhoisClient.DEFAULT_HOST); or lookup.icann.org don't work
            whois.connect("whois.arin.net");
            String result = whois.query(ipAddress);
            whois.disconnect();

            int start = result.indexOf("Organization:") + "Organization:   ".length();
            int end = result.indexOf("RegDate:") - 1;   // remove newline
            if(start >= 0 && end >= 1) {
                return new Org(0, result.substring(start, end), OrgType.CORPORATE.ordinal());
            }
            else return null;
        } catch (
                IOException e) {
            System.err.println("Error I/O exception: " + e.getMessage());
            return null;
        }
    }
}
