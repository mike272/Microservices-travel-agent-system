package com.rsww.hotelService.hotel;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface HotelRepository extends CrudRepository<Hotel, Integer>
{
    List<Hotel> findHotelsByLocation(String location);
}
