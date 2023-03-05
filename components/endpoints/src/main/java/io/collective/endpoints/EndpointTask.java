package io.collective.endpoints;

public class EndpointTask {
    private final String endpoint;

    private final long orgId;

    public EndpointTask(String endpoint, long id) {
        this.endpoint = endpoint;
        this.orgId = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public long getOrgId() {
        return orgId;
    }

    public String getAccept() {
        return "application/json";
    }
}