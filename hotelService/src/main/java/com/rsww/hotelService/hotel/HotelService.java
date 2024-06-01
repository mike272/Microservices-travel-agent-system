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


@Service
public class HotelService
{
    private final HotelRepositoryImpl hotelRepository;
    private final EventGateway eventGateway;

    public HotelService(final HotelRepositoryImpl hotelRepository, final EventGateway eventGateway)
    {
        this.hotelRepository = hotelRepository;
        this.eventGateway = eventGateway;
    }

    public com.rsww.dto.Hotel mapToDtoHotel(final Hotel hotelEntity)
    {
        return com.rsww.dto.Hotel.builder()
            .withDescription(hotelEntity.getDescription())
//            .withLocation(hotelEntity.getLocation())
            .withName(hotelEntity.getName())
            .withId(hotelEntity.getId())
//            .withMinPrice(hotelEntity.getMinPrice())
            .build();
    }

    public ArrayList<com.rsww.dto.Hotel> getHotelsByLocation(final String location)
    {
        return (ArrayList<com.rsww.dto.Hotel>) hotelRepository
            .findHotelsByLocation(location)
            .stream()
            .map(this::mapToDtoHotel)
            .toList();
    }

    public void initializeHotelsDatabase() throws IOException
    {
        final Reader reader = Files.newBufferedReader(Paths.get("./src/main/java/com/rsww/hotelService/hotel/scrapHotel.csv"));
        final CsvToBean<Hotel> csvToBean = new CsvToBeanBuilder<Hotel>(reader)
            .withType(Hotel.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        final List<Hotel> hotels = csvToBean.parse();
        final List<Hotel> hotelsSubList = hotels.subList(0, Math.min(10, hotels.size()));

        for (final Hotel hotel : hotels)
        {
            if (hotel.getDescription().length() > 200)
            {
                hotel.setDescription(hotel.getDescription().substring(0, 200));
            }
        }

        hotelRepository.saveAll(hotelsSubList);
        final var hotelsList = new ArrayList<>(hotelsSubList.stream().map(this::mapToDtoHotel).toList());
        eventGateway.publish(HotelsInitializedEvent.builder().withHotels(hotelsList).build());
    }
}
