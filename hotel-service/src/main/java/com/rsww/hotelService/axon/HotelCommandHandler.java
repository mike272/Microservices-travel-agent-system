package com.rsww.hotelService.axon;

import java.io.IOException;
import java.util.Objects;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import com.rsww.commands.CancelHotelReservationCommand;
import com.rsww.commands.ConfirmHotelReservationCommand;
import com.rsww.commands.InitializeHotelsCommand;
import com.rsww.commands.ReserveHotelCommand;
import com.rsww.dto.ReservationEventType;
import com.rsww.events.HotelReservationEvent;
import com.rsww.hotelService.hotel.HotelService;
import com.rsww.hotelService.reservation.ReservationService;


@Component
public class HotelCommandHandler
{

    private final HotelService hotelService;
    private final ReservationService reservationService;
    private final EventGateway eventGateway;

    public HotelCommandHandler(final HotelService hotelService, final ReservationService reservationService, final EventGateway eventGateway)
    {
        this.hotelService = hotelService;
        this.reservationService = reservationService;
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(final InitializeHotelsCommand command) throws IOException
    {
        hotelService.initializeHotelsDatabase();
    }

    @CommandHandler
    public void handle(final ReserveHotelCommand command)
    {
        final var hasReservationSucceeded = reservationService.reserveRooms(
            command.getTripReservationId(),
            command.getCustomerId(),
            command.getHotelId(),
            command.getCheckInDate(),
            command.getCheckOutDate(),
            command.getNumOfAdults(),
            command.getNumOfChildren(),
            command.getNumOfInfants()
        );
        if (Objects.equals(hasReservationSucceeded, "false"))
        {
            eventGateway.publish(HotelReservationEvent
                .builder()
                .withTripReservationId(command.getTripReservationId())
                .withHotelId(command.getHotelId())
                .withCustomerId(command.getCustomerId())
                .withCheckInDate(command.getCheckInDate())
                .withCheckOutDate(command.getCheckOutDate())
                .withStatus(ReservationEventType.FAILED)
                .build()
            );
        }

        final var reservationSucceededEvent = HotelReservationEvent
            .builder()
            .withTripReservationId(command.getTripReservationId())
            .withHotelId(command.getHotelId())
            .withCustomerId(command.getCustomerId())
            .withCheckInDate(command.getCheckInDate())
            .withCheckOutDate(command.getCheckOutDate())
            .withLocation(hasReservationSucceeded)
            .withDates(command.getCheckInDate().toString() + " - " + command.getCheckOutDate().toString())
            .withStatus(ReservationEventType.CREATED)
            .build();
        eventGateway.publish(reservationSucceededEvent);
    }

    @CommandHandler
    public void on(final ConfirmHotelReservationCommand command)
    {
        reservationService.confirmReservation(command.getTripReservationId());
    }

    @CommandHandler
    public void on(final CancelHotelReservationCommand command)
    {
        reservationService.cancelReservation(command.getTripReservationId());
    }

}