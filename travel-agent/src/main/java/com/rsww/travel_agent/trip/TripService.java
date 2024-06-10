package com.rsww.travel_agent.trip;

import java.util.ArrayList;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rsww.dto.ReservationEventType;
import com.rsww.events.TripCreatedEvent;
import com.rsww.events.TripReservationFailedEvent;
import com.rsww.responses.UsersTripsResponse;


@Service
public class TripService
{
    private static final Logger logger = LoggerFactory.getLogger(TripService.class);

    private final TripRepository tripRepository;
    private final EventGateway eventGateway;
    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public TripService(final TripRepository tripRepository, final EventGateway eventGateway, final QueryGateway queryGateway, final CommandGateway commandGateway)
    {
        this.tripRepository = tripRepository;
        this.eventGateway = eventGateway;
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    public com.rsww.dto.Trip mapToTripDTO(final Trip trip){
        return com.rsww.dto.Trip.builder()
            .withId(trip.getId())
            .withStatus(trip.getStatus())
            .withHotelId(trip.getHotelId())
            .withOutboundDate(trip.getOutboundDate())
            .withReturnDate(trip.getReturnDate())
            .withOutboundTransportId(trip.getOutboundTransportId())
            .withReturnTransportId(trip.getReturnTransportId())
            .withReturnTransportReservationId(trip.getReturnTransportReservationId())
            .withOutboundTransportReservationId(trip.getOutboundTransportReservationId())
            .withCustomerId(trip.getCustomerId())
            .withNumberOfAdults(trip.getNumberOfAdults())
            .withNumberOfChildren(trip.getNumberOfChildren())
            .withNumberOfInfants(trip.getNumberOfInfants())
            .withTotalPrice(trip.getTotalPrice())
            .withHotelReservationId(trip.getHotelReservationId())
            .build();
    }
    public Trip mapToDomainTrip(final com.rsww.dto.Trip trip){
        return Trip.builder()
            .withId(trip.getId())
            .withStatus(trip.getStatus())
            .withHotelId(trip.getHotelId())
            .withOutboundDate(trip.getOutboundDate())
            .withReturnDate(trip.getReturnDate())
            .withOutboundTransportId(trip.getOutboundTransportId())
            .withReturnTransportId(trip.getReturnTransportId())
            .withReturnTransportReservationId(trip.getReturnTransportReservationId())
            .withOutboundTransportReservationId(trip.getOutboundTransportReservationId())
            .withCustomerId(trip.getCustomerId())
            .withNumberOfAdults(trip.getNumberOfAdults())
            .withNumberOfChildren(trip.getNumberOfChildren())
            .withNumberOfInfants(trip.getNumberOfInfants())
            .withTotalPrice(trip.getTotalPrice())
            .withHotelReservationId(trip.getHotelReservationId())
            .build();
    }

    public UsersTripsResponse getUsersTrips(final int customerId)
    {
        logger.info("Getting trips for user: {}", customerId);
        return new UsersTripsResponse(new ArrayList<>(
            tripRepository
                .getTripsforUser(customerId)
                .stream()
                .map(this::mapToTripDTO)
                .toList()
        ));
    }

    public Trip createInitialReservation(final com.rsww.dto.Trip trip)
    {
        logger.info("Creating initial trip reservation for customer: {}",trip.getCustomerId());
        final Trip domainTrip = mapToDomainTrip(trip);
        trip.setStatus(ReservationEventType.CREATED);
        final var savedTrip = tripRepository.save(domainTrip);
        eventGateway.publish(TripCreatedEvent.builder()
            .withLocation(trip.getLocation())
            .withDates(trip.getOutboundDate().toString() + " - " + trip.getReturnDate().toString())
            .withTrip(mapToTripDTO(savedTrip)).build());
        return savedTrip;
    }

    public void confirmTripReservation(final int tripId){
        logger.info("Confirming trip reservation for trip: {}", tripId);
        final Trip trip = tripRepository.findById(tripId).orElseThrow(()->{
            logger.error("Trip not found for id: {}", tripId);
            eventGateway.publish(TripReservationFailedEvent.builder()
                .withTripReservationId(tripId)
                .withReason("Trip with id "+ tripId + "was not found in repository").build());
            return new RuntimeException("Trip not found for id: "+ tripId);
        });

        trip.setStatus(ReservationEventType.CONFIRMED);
        tripRepository.save(trip);
    }

    public void updateTripStatus(final int tripReservationId, final ReservationEventType status)
    {
        logger.info("Updating trip status for trip: {}", tripReservationId);
        final Trip trip = tripRepository.findById(tripReservationId).orElseThrow(()->{
            logger.error("Trip not found for id: {}", tripReservationId);
            eventGateway.publish(TripReservationFailedEvent.builder()
                .withTripReservationId(tripReservationId)
                .withReason("Trip with id "+ tripReservationId + "was not found in repository").build());
            return new RuntimeException("Trip not found for id: "+ tripReservationId);
        });

        trip.setStatus(status);
        tripRepository.save(trip);
    }
}
