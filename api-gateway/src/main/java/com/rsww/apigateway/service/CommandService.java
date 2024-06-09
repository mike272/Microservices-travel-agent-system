package com.rsww.apigateway.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rsww.commands.InitializeHotelsCommand;
import com.rsww.commands.InitializeTransportsCommand;
import com.rsww.commands.ReserveHotelCommand;
import com.rsww.commands.ReserveTripCommand;
import com.rsww.dto.ReservationConfirmation;
import com.rsww.dto.Trip;
import com.rsww.events.PaymentConfirmedEvent;


@Service
public class CommandService
{
    private final CommandGateway commandGateway;
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    public CommandService(final CommandGateway commandGateway)
    {
        this.commandGateway = commandGateway;
    }

    public ReservationConfirmation reserveTrip(final Trip trip)
        throws ExecutionException, InterruptedException, TimeoutException
    {
        final ReserveTripCommand reserveTripCommand = ReserveTripCommand
            .builder()
            .withTrip(trip)
            .build();

        final ReservationConfirmation reservationData = (ReservationConfirmation) commandGateway.send(reserveTripCommand).get(10, TimeUnit.SECONDS);
        logger.info("Reservation ID: {}", reservationData);
        return reservationData;
    }

    public void initializeHotels()
    {
        try
        {
            commandGateway.send(new InitializeHotelsCommand());
            commandGateway.send(new InitializeTransportsCommand());
        }
        catch (final Exception e)
        {
            logger.error("ERROR occurred while initializing hotels: ", e);

        }

    }

}
