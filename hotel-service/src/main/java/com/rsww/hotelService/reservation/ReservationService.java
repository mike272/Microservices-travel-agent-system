package com.rsww.hotelService.reservation;

import java.util.Date;
import java.util.List;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.rsww.dto.ReservationEventType;
import com.rsww.events.HotelReservationEvent;
import com.rsww.hotelService.hotel.Hotel;
import com.rsww.hotelService.hotel.HotelRepository;
import com.rsww.hotelService.room.Room;
import com.rsww.hotelService.room.RoomRepository;


@Service
public class ReservationService
{
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final EventGateway eventGateway;

    public ReservationService(final ReservationRepository reservationRepository,
                              final HotelRepository hotelRepository,
                              final RoomRepository roomRepository, final EventGateway eventGateway)
    {
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.eventGateway = eventGateway;
    }

    public void saveReservation(final Reservation reservation)
    {
        reservationRepository.save(reservation);
    }

    public boolean reserveRooms(final int tripReservationId,
                                final int clientId,
                                final int hotelId,
                                final Date checkInDate,
                                final Date checkOutDate,
                                final int numberOfAdults,
                                final int numberOfChildren,
                                final int numberOfInfants)
    {
        try
        {
            final var foundRooms = roomRepository.findRoomsByHotelIdAndGuests(hotelId, numberOfAdults + numberOfChildren + numberOfInfants);

            if (foundRooms.isEmpty())
            {
                eventGateway.publish(HotelReservationEvent.builder()
                    .withStatus(ReservationEventType.FAILED)
                    .withTripReservationId(tripReservationId)
                    .build());
                return false;
            }
            final var foundRoom = foundRooms.getFirst();
            final List<Reservation> existingReservations =
                reservationRepository.findReservationsByRoomIdAndDateRange(foundRoom.getId(), checkInDate, checkOutDate);
            if (!existingReservations.isEmpty())
            {
                return false;
            }

            final Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

            final Reservation reservation = Reservation
                .builder()
                .withTripReservationId(tripReservationId)
                .withClientId(clientId)
                .withFromDate(checkInDate)
                .withToDate(checkOutDate)
                .withNumberOfAdults(numberOfAdults)
                .withNumberOfChildren(numberOfChildren)
                .withNumberOfInfants(numberOfInfants)
                .withRoom(foundRoom)
                .withHotel(hotel)
                .withReservationStatus(ReservationEventType.CREATED)
                .build();
            saveReservation(reservation);
            return true;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    public void confirmReservation(final int tripReservationId)
    {
        final var reservations = reservationRepository.findByTripReservationId(tripReservationId);
        if (reservations.isEmpty())
        {
            throw new RuntimeException("No reservations found for trip: " + tripReservationId);
        }
        final var reservation = reservations.getFirst();
        reservation.setReservationStatus(ReservationEventType.CONFIRMED);
        saveReservation(reservation);
        final var hotelConfirmedEvent = HotelReservationEvent.builder()
            .withTripReservationId(tripReservationId)
            .withStatus(ReservationEventType.CONFIRMED)
            .build();
        eventGateway.publish(hotelConfirmedEvent);
    }

    public void cancelReservation(final int tripReservationId)
    {
        final var reservations = reservationRepository.findByTripReservationId(tripReservationId);
        if (reservations.isEmpty())
        {
            throw new RuntimeException("No reservations found for trip: " + tripReservationId);
        }
        final var reservation = reservations.getFirst();
        reservation.setReservationStatus(ReservationEventType.CANCELLED);
        saveReservation(reservation);
//        final var hotelConfirmedEvent = HotelReservationEvent.builder()
//            .withTripReservationId(tripReservationId)
//            .withStatus(ReservationEventType.CANCELLED)
//            .build();
//        eventGateway.publish(hotelConfirmedEvent);
    }
}
