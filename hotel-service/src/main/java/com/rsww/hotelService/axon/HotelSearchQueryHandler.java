package com.rsww.hotelService.axon;

import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rsww.hotelService.hotel.HotelService;
import com.rsww.hotelService.room.RoomService;
import com.rsww.queries.HotelSearchQuery;
import com.rsww.queries.TripSearchQuery;
import com.rsww.responses.AvailableHotelsResponse;


@Component
public class HotelSearchQueryHandler
{
    private static final Logger logger = LoggerFactory.getLogger(HotelSearchQueryHandler.class);
    private final RoomService roomService;
    private final HotelService hotelService;

    public HotelSearchQueryHandler(final RoomService roomService, final HotelService hotelService)
    {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    @QueryHandler
    public AvailableHotelsResponse handle(final TripSearchQuery query)
    {
        logger.info("Received TripSearchQuery: {}", query);
        return new AvailableHotelsResponse();
    }

    @QueryHandler
    public AvailableHotelsResponse handle(final HotelSearchQuery query)
    {
        logger.info("Received HotelSearchQuery: {}", query);
        final AvailableHotelsResponse hotels = AvailableHotelsResponse.builder()
            .withHotels(hotelService.getHotelsByLocation(query.getToLocation())
            ).build();
        logger.info("Returning {} hotels", (long) hotels.getHotels().size());
        // optionally check if those hotels have enough rooms available
        return hotels;
    }
}
