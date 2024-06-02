package com.rsww.hotelService.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


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
        final List<Room> allRooms = roomRepository.findRoomsByNumberOfGuests(numberOfAdults + numberOfChildren + numberOfInfants);
        return allRooms.stream()
            .filter(room -> room.getNumberOfPeople() >= numberOfAdults + numberOfChildren + numberOfInfants)
            .filter(room -> room.getReservations().stream()
                .noneMatch(reservation -> reservation.getFromDate().compareTo(checkInDate) <= 0
                    && reservation.getToDate().compareTo(checkOutDate) >= 0))
            .toList();
    }

    public List<Room> getAvailableRoomsForHotel(final int hotelId)
    {
        return roomRepository.findRoomsByHotelId(hotelId);
    }

    public List<Room> saveRooms(final List<Room> rooms)
    {
        return (List<Room>) roomRepository.saveAll(rooms);
    }

    public List<Room> parseRooms(final String csvRoomsList) throws JsonProcessingException
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String jsonRooms = csvRoomsList.replace("'", "\"");
        final var rooms = mapper.readValue(jsonRooms, new TypeReference<List<Room>>()
        {
        });
        rooms.forEach(room -> {
            room.setNumberOfPeople(room.getCapacityAdults() + room.getCapacityKids());
            room.setBasePrice(room.getPrice());
        });
        return rooms;
    }
}
