package com.rsww.apigateway.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsww.dto.Hotel;
import com.rsww.dto.Room;
import com.rsww.dto.TransportEvent;
import com.rsww.dto.Trip;
import com.rsww.queries.HotelInfoQuery;
import com.rsww.queries.HotelRoomsQuery;
import com.rsww.queries.HotelSearchQuery;
import com.rsww.queries.TransportEventsQuery;
import com.rsww.queries.TripSearchQuery;
import com.rsww.responses.AvailableHotelsResponse;
import com.rsww.responses.AvailableRoomsResponse;
import com.rsww.responses.AvailableTransportsResponse;
import com.rsww.responses.TripSearchResponse;


@Service
public class QueryService
{
    private final QueryGateway queryGateway;
    private static final Logger logger = LoggerFactory.getLogger(QueryService.class);

    @Autowired
    public QueryService(final QueryGateway queryGateway)
    {
        this.queryGateway = queryGateway;
    }

    public List<Trip> forwardSearchTrips(final String fromLocation, final String toLocation, final Date fromDate, final Date toDate) throws InterruptedException, ExecutionException, TimeoutException
    {
        logger.info("Searching trips from {} to {} from {} to {}", fromLocation, toLocation, fromDate, toDate);
        final TripSearchQuery query = TripSearchQuery
            .builder()
            .withFromDate(fromDate)
            .withFromLocation(fromLocation)
            .withToDate(toDate)
            .withToLocation(toLocation)
            .build();

        return queryGateway.query(query, TripSearchResponse.class).get(10, TimeUnit.SECONDS).getTrips();
    }
    public AvailableHotelsResponse forwardSearchHotels(@Nullable final String toLocation,@Nullable final Date fromDate,@Nullable final Date toDate) throws InterruptedException, ExecutionException, TimeoutException
    {
        logger.info("Searching hotels in {} from {} to {}", toLocation, fromDate, toDate);
        final HotelSearchQuery query = HotelSearchQuery
            .builder()
            .withFromDate(fromDate)
            .withToDate(toDate)
            .withToLocation(toLocation)
            .build();

        return queryGateway.query(query, AvailableHotelsResponse.class).get(10, TimeUnit.SECONDS);
    }

    public List<Hotel> getHotelDetails(final int hotelId)
    {
        final HotelInfoQuery query = HotelInfoQuery.builder().withHotelId(hotelId).build();
        final AvailableHotelsResponse response = queryGateway.query(query, AvailableHotelsResponse.class).join();
        return response.getHotels();
    }

    public List<Room> getRooms(final int hotelId, final Date fromDate, final Date toDate, final int numOfPeople)
    {
        final HotelRoomsQuery query = HotelRoomsQuery.builder().withHotelId(hotelId).withCheckInDate(fromDate).withCheckOutDate(toDate).withNumberOfGuests(numOfPeople).build();
        final AvailableRoomsResponse response = queryGateway.query(query, AvailableRoomsResponse.class).join();
        return response.getRooms();

    }

    public List<TransportEvent> getTransports(final String fromLocation, final String toLocation, final Date fromDate, final Date toDate)
    {
        final TransportEventsQuery query = TransportEventsQuery.builder()
            .withFromLocation(fromLocation)
            .withToLocation(toLocation)
            .withFromDate(fromDate)
            .withToDate(toDate)
            .build();
        final AvailableTransportsResponse response = queryGateway.query(query, AvailableTransportsResponse.class).join();
        return response.getTransports();
    }
}
