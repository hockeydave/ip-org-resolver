package test.collective.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.collective.json.Item;
import io.collective.json.Json;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class JsonTest {


    @Test
    public void json() throws IOException {
        String jsonString = new String(Objects.requireNonNull(this.getClass()
                .getResourceAsStream("/arin-registry-entity-CC-3517.json")).readAllBytes());
        JsonMapper mapper = new JsonMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Json json = mapper.readValue(jsonString, Json.class);
        List<Item> networks = json.getNetworks();
        assertEquals(229, networks.size());
    }
}
