package com.rsww.hotelService.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAvailableRooms(Date checkInDate, Date checkOutDate, int numberOfAdults, int numberOfChildren, int numberOfInfants) {
        List<Room> allRooms = roomRepository.findRoomsByNumberOfGuests(numberOfAdults, numberOfChildren, numberOfInfants);
        return allRooms.stream()
            .filter(room -> room.getNumberOfPeople() >= numberOfAdults + numberOfChildren + numberOfInfants)
            .filter(room -> room.getReservations().stream()
                .noneMatch(reservation -> reservation.getFromDate().compareTo(checkInDate) <= 0 && reservation.getToDate().compareTo(checkOutDate) >= 0))
            .collect(Collectors.toList());
    }
}
