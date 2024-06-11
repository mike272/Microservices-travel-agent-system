package com.rsww.travel_agent.event_generator;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import com.rsww.dto.ReservationEventType;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.travel_agent.trip.Trip;
import com.rsww.travel_agent.trip.TripService;


@Service
public class EventGenerator {

    private final TripService tripService;
    private final EventGateway eventGateway;
    private final List<Integer> tripReservationIds = new ArrayList<>();
    private final Random random = new Random();
    List<Integer> ids = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());

    public EventGenerator(final TripService tripService, final EventGateway eventGateway) {
        this.tripService = tripService;
        this.eventGateway = eventGateway;
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        // Schedule createInitialReservation every 5 seconds
        executorService.scheduleAtFixedRate(this::createInitialReservation, 0, 20, TimeUnit.SECONDS);

        // Schedule paymentSucceeded every 10 seconds
        executorService.scheduleAtFixedRate(this::paymentSucceeded, 0, 40, TimeUnit.SECONDS);
    }

    private void createInitialReservation() {
        final com.rsww.dto.Trip trip = new com.rsww.dto.Trip();
        trip.setCustomerId(200);
        final Random rand = new Random();
        trip.setHotelId(ids.get(rand.nextInt(ids.size())));
        trip.setOutboundTransportId(ids.get(rand.nextInt(ids.size())));
        trip.setReturnTransportId(ids.get(rand.nextInt(ids.size())));
        trip.setNumberOfAdults(3);
        trip.setLocation("RANDOM_LOCATION");
        trip.setOutboundDate(new Date());
        trip.setReturnDate(new Date());

        final Trip createdTrip = tripService.createInitialReservation(trip);
        tripReservationIds.add(createdTrip.getId());
    }

    private void paymentSucceeded() {
        if (!tripReservationIds.isEmpty()) {
            final int randomIndex = random.nextInt(tripReservationIds.size());
            final int tripReservationId = tripReservationIds.get(randomIndex);
            eventGateway.publish(PaymentConfirmedEvent.builder().withTripReservationId(tripReservationId).build());
        }
    }
}
