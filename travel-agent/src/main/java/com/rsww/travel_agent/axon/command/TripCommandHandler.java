package com.rsww.travel_agent.axon.command;

import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import com.rsww.commands.CancelReservationCommand;
import com.rsww.commands.ReserveTripCommand;
import com.rsww.commands.UpdateTripStatusCommand;
import com.rsww.dto.ReservationConfirmation;
import com.rsww.dto.ReservationEventType;
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
    public ReservationConfirmation on(final ReserveTripCommand command)
    {
        final var trip = tripService.createInitialReservation(command.getTrip());
        if(trip == null)
        {
            return ReservationConfirmation.builder().withStatus(ReservationEventType.FAILED).build();
        }
        return ReservationConfirmation.builder().withMessage(
            " to " + trip.getLocation() + " on " +
                trip.getOutboundDate().toString()+"-"+trip.getReturnDate().toString() +
                " for " + trip.getNumberOfAdults()+trip.getNumberOfChildren()+trip.getNumberOfInfants() + " people "
        ).withStatus(ReservationEventType.CREATED).withReservationId(trip.getId()).build();
    }

    @CommandHandler
    public void on(final UpdateTripStatusCommand command){
        tripService.updateTripStatus(command.getTripReservationId(), command.getStatus());
    }
    @CommandHandler
    public void on(final CancelReservationCommand command){
        tripService.updateTripStatus(command.getTripReservationId(),ReservationEventType.CANCELLED);
    }


}
