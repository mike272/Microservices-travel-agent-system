package com.rsww.hotelService.reservation;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface ReservationRepository extends CrudRepository<Reservation, Integer>
{
    public List<Reservation> getReservationsByRoomId(int roomId);

    List<Reservation> findReservationsByRoomIdAndDateRange(final int roomId, final Date fromDate, final Date toDate);

    List<Reservation> findByTripReservationId(int tripReservationId);
}
