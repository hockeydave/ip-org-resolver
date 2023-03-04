package io.collective.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JsonProperty
    private String startAddress;

    @JsonGetter("startAddress")
    public String getStartAddress() {
        return startAddress;
    }

    @JsonProperty
    private String endAddress;

    @JsonGetter("endAddress")
    public String getEndAddress() {
        return endAddress;
    }

}
