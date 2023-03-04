package io.collective.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Json {
    @JsonProperty
    private List<Item> networks;

    public List<Item> getNetworks() {
        return networks;
    }
}
