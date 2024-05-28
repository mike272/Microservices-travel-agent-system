package com.rsww.hotelService.reservation;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rsww.dto.ReservationEventType;
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

    public ReservationService(final ReservationRepository reservationRepository,
                              final HotelRepository hotelRepository,
                              final RoomRepository roomRepository)
    {
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public void saveReservation(final Reservation reservation)
    {
        reservationRepository.save(reservation);
    }

    public boolean reserveRooms(final int clientId,
                                final int hotelId,
                                final List<Integer> roomIds,
                                final Date checkInDate,
                                final Date checkOutDate,
                                final int numberOfAdults,
                                final int numberOfChildren,
                                final int numberOfInfants)
    {
        try
        {
            for (final Integer roomId : roomIds)
            {
                final List<Reservation> existingReservations =
                    reservationRepository.findReservationsByRoomIdAndDateRange(roomId, checkInDate, checkOutDate);
                if (!existingReservations.isEmpty())
                {
                    return false;
                }
            }
            final Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

            roomIds.stream()
                .map(roomId -> {
                    final Room room = roomRepository.findById(roomId)
                        .orElseThrow(() -> new RuntimeException("Room not found"));
                    return Reservation
                        .builder()
                        .withClientId(clientId)
                        .withFromDate(checkInDate)
                        .withToDate(checkOutDate)
                        .withNumberOfAdults(numberOfAdults)
                        .withNumberOfChildren(numberOfChildren)
                        .withNumberOfInfants(numberOfInfants)
                        .withRoom(room)
                        .withHotel(hotel)
                        .withReservationStatus(ReservationEventType.CREATED)
                        .build();
                }).forEach(this::saveReservation);
            return true;
        }
        catch (final Exception e)
        {
            return false;
        }
    }
}
