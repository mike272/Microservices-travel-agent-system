package com.rsww.hotelService.hotel;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rsww.events.HotelsInitializedEvent;
import com.rsww.events.RoomsInitializedEvent;
import com.rsww.hotelService.room.Room;
import com.rsww.hotelService.room.RoomService;


@Service
public class HotelService
{
    private final HotelRepository hotelRepository;
    private final EventGateway eventGateway;
    private final RoomService roomService;

    public HotelService(final HotelRepository hotelRepository, final EventGateway eventGateway, final RoomService roomService)
    {
        this.hotelRepository = hotelRepository;
        this.eventGateway = eventGateway;
        this.roomService = roomService;
    }

    public com.rsww.dto.Hotel mapToDtoHotel(final Hotel hotelEntity)
    {
        return com.rsww.dto.Hotel.builder()
            .withDescription(hotelEntity.getDescription())
            .withCountry(hotelEntity.getCountry())
            .withCity(hotelEntity.getCity())
            .withName(hotelEntity.getName())
            .withId(hotelEntity.getId())
            .withMinPrice(hotelEntity.getMinPrice())
            .withStarsNum(hotelEntity.getStarsNum())
            .build();
    }

    public ArrayList<com.rsww.dto.Hotel> getHotelsByLocation(final String location)
    {
        final var hotels = new ArrayList<>(hotelRepository
            .findHotelsByLocation(location)
            .stream()
            .map(this::mapToDtoHotel)
            .toList());

        hotels.forEach(hotel -> {
            final var rooms = roomService.getAvailableRoomsForHotel(hotel.getId());
            final var lowestPrice = rooms.stream().map(Room::getBasePrice).min(Double::compareTo).orElse(0.0);
            hotel.setMinPrice(lowestPrice);
        });

        return hotels;
    }

    public void initializeHotelsDatabase() throws IOException
    {
        final Reader reader = Files.newBufferedReader(Paths.get("./src/main/java/com/rsww/hotelService/hotel/scrapHotel.csv"));
        final CsvToBean<Hotel> csvToBean = new CsvToBeanBuilder<Hotel>(reader)
            .withType(Hotel.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        final long count = hotelRepository.count();
        final List<Hotel> hotels = csvToBean.parse();
        final List<Hotel> hotelsSubList = hotels.stream().skip(count).limit(50).toList();
        final List<Room> allRooms = new ArrayList<>();
//        I think I need to save the hotels before i save rooms and put the returned hotel objects because of the id property that is being generated on save
//        final List<Hotel> savedHotels = new ArrayList<>();

        for (final Hotel hotel : hotelsSubList)
        {
            if (hotel.getDescription().length() > 200)
            {
                hotel.setDescription(hotel.getDescription().substring(0, 200));
            }
            final List<Room> roomsForHotel = new ArrayList<>();
            final List<Room> rooms = roomService.parseRooms(hotel.getRoomsTmp());
            // first save hotel without rooms
//            final var savedHotel = hotelRepository.save(hotel);
//            savedHotels.add(savedHotel);
            // create rooms and assign the newly created hotel to them
            for (final Room room : rooms)
            {
//                room.setHotel(savedHotel);

                for (int i = 0; i < room.getAvailableRooms(); i++)
                {
                    // in csv we specify how many rooms of each type there are. here I create that number of rooms and insert them into db
                    final Room newRoom = Room.builder()
                        .withHotel(hotel)
                        .withBasePrice(room.getBasePrice())
                        .withCapacityKids(room.getCapacityKids())
                        .withPrice(room.getPrice())
                        .withRoomName(room.getRoomName())
                        .withCapacityAdults(room.getCapacityAdults())
                        .withNumberOfPeople(room.getNumberOfPeople())
                        .build();
                    roomsForHotel.add(newRoom);
                    allRooms.add(newRoom);
                }
            }
            // save rooms for hotel
//            final var savedRooms = roomService.saveRooms(roomsForHotel);
            // add saved rooms to hotel
            hotel.setRooms(roomsForHotel);
//            roomService.saveRooms(roomsForHotel);
            hotelRepository.saveAndFlush(hotel);
        }
        final var hotelsList = new ArrayList<>(hotelsSubList.stream().map(this::mapToDtoHotel).toList());
        eventGateway.publish(HotelsInitializedEvent.builder().withHotels(hotelsList).build());
        eventGateway.publish(RoomsInitializedEvent.builder().withNumberOfRooms(allRooms.size()).build());
    }
}
