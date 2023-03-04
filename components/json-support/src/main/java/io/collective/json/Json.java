package io.collective.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Json {
    @JsonProperty
    private Networks networks;

    public Networks getNetworks() {
        return networks;
    }
}
