package io.collective.ip

import io.collective.database.devDataSource
import io.collective.endpoints.EndpointTask
import io.collective.endpoints.EndpointWorker
import io.collective.entities.Org
import io.collective.entities.OrgIPDataGateway
import io.collective.entities.OrgIPService
import io.collective.restsupport.RestTemplate

class IpToOrgResolver {

    fun getOrg(ipAddress: String): Org {

            val orgIpService = OrgIPService(OrgIPDataGateway(devDataSource()))
            // First check the database to see if this IP is mapped to an Org
            var org = orgIpService.findByIp(ipAddress)
            if (org == null) {
                // If not in the database, get the Org from the web API
                val fullOrg = IpToOrgWebAPIResolver().getOrg(ipAddress)
                // Check if the org is in the database, but just missing the update for this IP range
                val newOrg: Org? = orgIpService.findByName(fullOrg.org.name)
                org = fullOrg.org
                if (newOrg == null) {
                    // Org is missing from database so create it
                    val orgCreated = orgIpService.createOrg(fullOrg.org.name, fullOrg.org.orgType)

                    val template = RestTemplate()
                    val gateway = OrgIPDataGateway(devDataSource())


                    val worker = EndpointWorker(template, gateway)
                    worker.execute(EndpointTask(fullOrg.url, orgCreated.id))
                } // TODO else, update the Org for this IP range.
            }
            return org
        }
}