package io.collective.json;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Networks {
    @JsonProperty
    private List<Item> items;
    @JsonGetter
    public List<Item> getItems() {
        return items;
    }
}
