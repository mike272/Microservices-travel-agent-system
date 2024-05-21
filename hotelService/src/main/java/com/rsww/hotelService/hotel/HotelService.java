package com.rsww.hotelService.hotel;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rsww.dto.ReservationEventType;


@Service
public class HotelService
{
    private final HotelRepositoryImpl hotelRepository;

    @Autowired
    public HotelService(final HotelRepositoryImpl hotelRepository)
    {
        this.hotelRepository = hotelRepository;
    }

    public com.rsww.dto.Hotel mapToDtoHotel(final Hotel hotelEntity) {
        return com.rsww.dto.Hotel.builder()
            .withDescription(hotelEntity.getDescription())
//            .withLocation(hotelEntity.getLocation())
            .withName(hotelEntity.getName())
            .withId(hotelEntity.getId())
//            .withMinPrice(hotelEntity.getMinPrice())
            .build();
    }

    public List<com.rsww.dto.Hotel> getHotelsByLocation(final String location)
    {
        return hotelRepository
            .findAll()
            .stream()
            .map(this::mapToDtoHotel)
            .collect(Collectors.toList());
    }

    public List<com.rsww.dto.Hotel> initializeHotelsDatabase() throws IOException
    {
        final Reader reader = Files.newBufferedReader(Paths.get("./scrapHotel.csv"));
        final CsvToBean<Hotel> csvToBean = new CsvToBeanBuilder<Hotel>(reader)
            .withType(Hotel.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        final List<Hotel> hotels = csvToBean.parse();
        final List<Hotel> hotelsSubList = hotels.subList(0, Math.min(10, hotels.size()));

        hotelRepository.saveAll(hotelsSubList);
        return hotelsSubList.stream().map(this::mapToDtoHotel).collect(Collectors.toList());
    }
}
