package test.collective.endpoints;

import io.collective.entities.*;
import io.collective.endpoints.EndpointTask;
import io.collective.endpoints.EndpointWorker;
import io.collective.restsupport.RestTemplate;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static io.collective.testsupport.TestDataSourceKt.testDataSource;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EndpointWorkerTest {
    @Test
    public void finder() throws IOException, NullPointerException {
        String json = new String(Objects.requireNonNull(getClass()
                .getResourceAsStream("/arin-registry-entity-CC-3517.json")).readAllBytes());

        RestTemplate mock = mock(RestTemplate.class);
        when(mock.get("https://rdap.arin.net/registry/entity/CC-3517", "application/json")).thenReturn(json);
        OrgIPDataGateway gateway = new OrgIPDataGateway(testDataSource());

        EndpointWorker worker = new EndpointWorker(mock, gateway);
        worker.execute(new EndpointTask("https://rdap.arin.net/registry/entity/CC-3517"));

        assertEquals(229, gateway.findAll().size());

    }
}