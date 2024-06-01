package com.rsww.transport_service.axon;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.rsww.queries.TransportSearchQuery;
import com.rsww.responses.AvailableTransportsResponse;
import com.rsww.transport_service.transport.Transport;
import com.rsww.transport_service.transport.TransportService;


@Service
public class TransportQueryHandler
{
    private final TransportService transportService;
    private final EventGateway eventGateway;
    private final CommandGateway commandGateway;

    public TransportQueryHandler(final TransportService transportService,
                                 final EventGateway eventGateway,
                                 final CommandGateway commandGateway)
    {
        this.transportService = transportService;
        this.eventGateway = eventGateway;
        this.commandGateway = commandGateway;
    }

    @QueryHandler
    public AvailableTransportsResponse handle(final TransportSearchQuery query)
    {
        return transportService.findAvailableTransports(query.getFromLocation(),
            query.getToLocation(),
            query.getFromDate(),
            query.getToDate(),
            query.getNumOfAdults(),
            query.getNumOfChildren(),
            query.getNumOfInfants());
    }

}