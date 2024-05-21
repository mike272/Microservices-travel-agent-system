package com.rsww.hotelService.room;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rsww.hotelService.room.Room;


public interface RoomRepository extends CrudRepository<Room, Integer>
{
    List<Room> findRoomsByNumberOfGuests(int numberOfAdults, int numberOfChildren, int numberOfInfants);
}