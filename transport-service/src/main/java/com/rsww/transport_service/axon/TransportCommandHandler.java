package com.rsww.transport_service.axon;

import java.io.IOException;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.rsww.commands.InitializeHotels;
import com.rsww.commands.InitializeTransportsCommand;
import com.rsww.transport_service.transport.TransportService;


@Service
public class TransportCommandHandler{

    private final TransportService transportService;
    private final EventGateway eventGateway;
    private final CommandGateway commandGateway;

    public TransportCommandHandler(final TransportService transportService, final EventGateway eventGateway, final CommandGateway commandGateway) {
        this.transportService = transportService;
        this.eventGateway = eventGateway;
        this.commandGateway = commandGateway;
    }

    @CommandHandler
    public void handle(final InitializeTransportsCommand command) throws IOException
    {
        transportService.initializeTransports();
    }

}

