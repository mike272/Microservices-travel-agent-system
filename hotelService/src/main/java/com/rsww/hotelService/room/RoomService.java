package com.rsww.hotelService.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoomService
{

    private final RoomRepository roomRepository;

    public RoomService(final RoomRepository roomRepository)
    {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAvailableRooms(final Date checkInDate,
                                        final Date checkOutDate,
                                        final int numberOfAdults,
                                        final int numberOfChildren,
                                        final int numberOfInfants)
    {
        final List<Room> allRooms = roomRepository.findRoomsByNumberOfGuests(numberOfAdults, numberOfChildren, numberOfInfants);
        return allRooms.stream()
            .filter(room -> room.getNumberOfPeople() >= numberOfAdults + numberOfChildren + numberOfInfants)
            .filter(room -> room.getReservations().stream()
                .noneMatch(reservation -> reservation.getFromDate().compareTo(checkInDate) <= 0
                    && reservation.getToDate().compareTo(checkOutDate) >= 0))
            .toList();
    }
}
