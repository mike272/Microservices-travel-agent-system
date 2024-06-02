package com.rsww.hotelService.hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HotelRepository extends JpaRepository<Hotel, Integer>
{
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.city) = LOWER(:location) OR LOWER(h.country) = LOWER(:location)")
    List<Hotel> findHotelsByLocation(@Param("location") String location);
}
