package com.rsww.travel_agent.axon.command;

import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import com.rsww.commands.ReserveTripCommand;
import com.rsww.travel_agent.trip.TripService;


@Service
public class TripCommandHandler
{
    private final TripService tripService;

    public TripCommandHandler(final TripService tripService)
    {
        this.tripService = tripService;
    }

    @CommandHandler
    public void on(final ReserveTripCommand command)
    {
        tripService.createInitialReservation(command.getTrip());
    }


}
