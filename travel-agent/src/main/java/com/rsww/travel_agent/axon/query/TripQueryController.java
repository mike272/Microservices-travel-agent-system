package com.rsww.travel_agent.axon.query;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rsww.queries.UsersTripsQuery;
import com.rsww.responses.UsersTripsResponse;
import com.rsww.travel_agent.trip.TripService;


@Component
public class TripQueryController
{
    private final TripService service;

    public TripQueryController(final TripService service)
    {
        this.service = service;
    }

    private static final Logger logger = LoggerFactory.getLogger(TripQueryController.class);
    @QueryHandler
    public UsersTripsResponse handle(final UsersTripsQuery query) throws ExecutionException, InterruptedException, TimeoutException
    {
        logger.info("Received UsersTripsQuery for user: {}", query.getCustomerId());
        return service.getUsersTrips(query.getCustomerId());
    }
}
