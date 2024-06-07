package com.rsww.travel_agent.axon.command;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rsww.commands.ReserveTripCommand;
import com.rsww.dto.ReservationConfirmation;


@Service
public class HotelCommandService
{
    private final CommandGateway commandGateway;
    private static final Logger logger = LoggerFactory.getLogger(HotelCommandService.class);

    public HotelCommandService(final CommandGateway commandGateway)
    {
        this.commandGateway = commandGateway;
    }

    private ReservationConfirmation reserveTrip(final int tripId, final int hotelId, final int transportEventId)
        throws ExecutionException, InterruptedException, TimeoutException
    {
        final ReserveTripCommand reserveTripCommand = ReserveTripCommand
            .builder()
            .withTripId(tripId)
            .withHotelId(hotelId)
            .withTransportId(transportEventId)
            .build();
        final ReservationConfirmation reservationData = (ReservationConfirmation) commandGateway.send(reserveTripCommand).get(10, TimeUnit.SECONDS);
        logger.info("Reservation ID: {}", reservationData);
        return reservationData;
    }


}