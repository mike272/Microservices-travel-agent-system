package com.rsww.transport_service.axon;

import java.io.IOException;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.rsww.commands.CancelTransportReservationCommand;
import com.rsww.commands.ConfirmTransportReservationCommand;
import com.rsww.commands.InitializeTransportsCommand;
import com.rsww.commands.ReserveTransportCommand;
import com.rsww.transport_service.transport.TransportService;


@Service
public class TransportCommandHandler
{

    private final TransportService transportService;
    private final EventGateway eventGateway;
    private final CommandGateway commandGateway;

    public TransportCommandHandler(final TransportService transportService, final EventGateway eventGateway, final CommandGateway commandGateway)
    {
        this.transportService = transportService;
        this.eventGateway = eventGateway;
        this.commandGateway = commandGateway;
    }

    @CommandHandler
    public void handle(final InitializeTransportsCommand command) throws IOException
    {
        transportService.initializeTransports();
    }

    @CommandHandler
    public void handle(final ReserveTransportCommand command)
    {
        transportService.reserveTransports(command.getTripReservationId(),
            command.getOutboundTransportId(),
            command.getOutboundDate(),
            command.getReturnTransportId(),
            command.getReturnDate(),
            command.getNumOfAdults(),
            command.getNumOfChildren(),
            command.getNumOfInfants()
        );

    }

    @CommandHandler
    public void on(final ConfirmTransportReservationCommand command)
    {
        transportService.confirmTransportReservation(command.getTripReservationId());
    }

    @CommandHandler
    public void on(final CancelTransportReservationCommand command)
    {
//        nothing to do here at the moment
//        transportService.cancelTransportReservation(command.getTripReservationId());
    }
}

