package io.collective.endpoints;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.collective.entities.IPUtility;
import io.collective.entities.OrgIPDataGateway;
import io.collective.json.Item;
import io.collective.json.Json;
import io.collective.restsupport.RestTemplate;
import io.collective.workflow.Worker;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class EndpointWorker implements Worker<EndpointTask> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate template;
    private final OrgIPDataGateway gateway;

    public EndpointWorker(RestTemplate template, OrgIPDataGateway gateway) {
        this.template = template;
        this.gateway = gateway;
    }

    @NotNull
    @Override
    public String getName() {
        return "ready";
    }


    /**
     * @param task EndpointTask to work on
     *             Map rss results to an OrgIpRecord item collection and save OrgIpRecord items to the OrgIpDataGateway and
     *             save to it's DataSource (i.e. database).
     */
    @Override
    public void execute(EndpointTask task) {
        String response = template.get(task.getEndpoint(), task.getAccept());
        {
            try {
                JsonMapper mapper = new JsonMapper();
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                Json json = mapper.readValue(response, Json.class);
                for (Item item : json.getNetworks()) {
                    // TODO fix orgId
                    BigInteger startAddress = IPUtility.convertIPtoBigInteger(item.getStartAddress());
                    BigInteger endAddress = IPUtility.convertIPtoBigInteger(item.getEndAddress());
                    gateway.save(startAddress, endAddress, task.getOrgId());
                }
            } catch (Exception e) {
                logger.error(e + " .Cannot process JSON string for Task" + response);
            }
        }
    }
}
