package test.collective.endpoints;

import io.collective.entities.*;
import io.collective.endpoints.EndpointTask;
import io.collective.endpoints.EndpointWorker;
import io.collective.restsupport.RestTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static io.collective.testsupport.TestDataSourceKt.testDataSource;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EndpointWorkerTest {
    private OrgIPDataGateway gateway;
    private Org org;

    @Before
    public void before() {
        gateway = new OrgIPDataGateway(testDataSource());
        org = gateway.createOrg("test org - EndpointWorkerTest", OrgType.CLOUD.ordinal());
    }

    @After
    public void after() {
        gateway.clear(org.getId());
    }

    @Test
    public void finder() throws IOException, NullPointerException {

        String json = new String(Objects.requireNonNull(getClass()
                .getResourceAsStream("/arin-registry-entity-CC-3517.json")).readAllBytes());

        RestTemplate mock = mock(RestTemplate.class);
        when(mock.get("https://rdap.arin.net/registry/entity/CC-3517", "application/json")).thenReturn(json);
        OrgIPDataGateway gateway = new OrgIPDataGateway(testDataSource());


        EndpointWorker worker = new EndpointWorker(mock, gateway);
        worker.execute(new EndpointTask("https://rdap.arin.net/registry/entity/CC-3517", org.getId()));
        String m1 = String.format("EndpointWorkerTest failed on org_ip count with org_id = %d", org.getId());

        assertEquals(m1, 229, gateway.findAllByOrg(org.getId()).size());
    }
}