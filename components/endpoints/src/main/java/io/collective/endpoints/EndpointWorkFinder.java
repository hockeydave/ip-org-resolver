package io.collective.endpoints;

import io.collective.workflow.WorkFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class EndpointWorkFinder implements WorkFinder<EndpointTask> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EndpointDataGateway gateway;

    public EndpointWorkFinder(EndpointDataGateway gateway) {
        this.gateway = gateway;
    }

    @NotNull
    @Override
    public List<EndpointTask> findRequested(@NotNull String name) {
        long orgId = 2; // TODO fix orgId
        return gateway.findReady(name).stream()
                .map(record -> new EndpointTask(record.getName(), orgId)).collect(Collectors.toList());
    }

    @Override
    public void markCompleted(EndpointTask info) {
        logger.info("marking work complete.");
    }
}