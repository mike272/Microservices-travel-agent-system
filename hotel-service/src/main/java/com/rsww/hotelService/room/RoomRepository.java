package com.rsww.hotelService.room;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rsww.hotelService.room.Room;


public interface RoomRepository extends CrudRepository<Room, Integer>
{
    @Query(
        "SELECT r FROM Room r WHERE r.numberOfPeople >= :totalGuests")
    List<Room> findRoomsByNumberOfGuests(int totalGuests);

    @Query(
        "SELECT t FROM Room t WHERE t.hotel.id = :hotelId")
    List<Room> findRoomsByHotelId(int hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.numberOfPeople >= :totalGuests")
    List<Room> findRoomsByHotelIdAndGuests(@Param("hotelId") int hotelId, @Param("totalGuests") int totalGuests);
//    @Query(
//        "SELECT t FROM Room t WHERE "
//           )
//    List<Room> findFlights(@Param("departureLocation") String departureLocation,
//                                @Param("arrivalLocation") String arrivalLocation,
//                                @Param("departureDate") Date departureDate,
//                                Pageable pageable);
}