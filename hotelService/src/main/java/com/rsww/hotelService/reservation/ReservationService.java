package com.rsww.hotelService.reservation;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rsww.dto.ReservationEventType;


@Service
public class ReservationService
{
    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository)
    {
        this.reservationRepository = reservationRepository;
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
            for (final Integer roomId : roomIds) {
                final List<Reservation> existingReservations = reservationRepository.findReservationsByRoomIdAndDateRange(roomId, checkInDate, checkOutDate);
                if (!existingReservations.isEmpty()) {
                    return false;
                }
            }

            roomIds.stream()
                .map(roomId -> Reservation
                    .builder()
                    .withClientId(clientId)
                    .withFromDate(checkInDate)
                    .withToDate(checkOutDate)
                    .withNumberOfAdults(numberOfAdults)
                    .withNumberOfChildren(numberOfChildren)
                    .withNumberOfInfants(numberOfInfants)
                    .withRoomId(roomId)
                    .withHotelId(hotelId)
                    .withReservationStatus(ReservationEventType.CREATED)
                    .build()
                ).forEach(this::saveReservation);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
