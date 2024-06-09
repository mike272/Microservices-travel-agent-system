package com.rsww.apigateway.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.rsww.apigateway.query.TripSearchQuery;
import com.rsww.apigateway.service.CommandService;
import com.rsww.apigateway.service.QueryService;
import com.rsww.commands.ReserveTripCommand;
import com.rsww.dto.ReservationConfirmation;
import com.rsww.dto.Room;
import com.rsww.dto.Trip;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.events.PaymentFailedEvent;
import com.rsww.queries.TripSearchQuery;

import lombok.Data;


@Data
@RestController
@CrossOrigin
@RequestMapping("/v1/trips")
public class TravelAgentController
{
    private static final Logger logger = LoggerFactory.getLogger(TravelAgentController.class);
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

    private final QueryService queryService;
    private final CommandService commandService;
    private final EventGateway eventGateway;

    public TravelAgentController(final QueryService queryService, final CommandService commandService, final EventGateway eventGateway)
    {
        this.queryService = queryService;
        this.commandService = commandService;
        this.eventGateway = eventGateway;
    }

    private Date getTomorrowDate()
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    // endpoint for searching trips with data in query parameters
    @GetMapping(value = "/search")
    public ResponseEntity<List<Trip>> searchTrips(
        @RequestParam(required = false) final String fromDate,
        @RequestParam(required = false) final String fromLocation,
        @RequestParam(required = false) final String toDate,
        @RequestParam(required = false) final String toLocation,
        @RequestParam(required = false) final Integer adults,
        @RequestParam(required = false) final Integer children,
        @RequestParam(required = false) final Integer infants
    ) throws InterruptedException, ExecutionException, TimeoutException
    {
        logger.info("Received TripSearchQuery: fromDate={}, fromLocation={}, toDate={}, toLocation={}, numOfPeople={}",
            fromDate,
            fromLocation,
            toDate,
            toLocation,
            adults + children + infants);
        final Date fromDateCast = fromDate == null || fromDate.isEmpty() ? new Date() : new Date(fromDate);
        final Date toDateCast = toDate == null || toDate.isEmpty() ? getTomorrowDate() : new Date(toDate);

        final List<Trip> trips = queryService.forwardSearchTrips(fromLocation, toLocation, fromDateCast, toDateCast, adults, children, infants);

        return ResponseEntity.ok(trips);
    }

    // TODO:: move service functions from this project to Travel Agent project
    @GetMapping(value = "/hotels/{hotelId}/rooms")
    public ResponseEntity<Object> getRooms(
        @PathVariable final String hotelId,
        @RequestParam final String fromDate,
        @RequestParam final String toDate,
        @RequestParam(required = false) final Integer adults,
        @RequestParam(required = false) final Integer children,
        @RequestParam(required = false) final Integer infants)
    {
        logger.info("Received request for getting rooms in hotelId={}, fromDate={}, toDate={}, numberOfPeople={}",
            hotelId,
            fromDate,
            toDate,
            adults + children + infants);
        try
        {
            // parse params
            final int hotelIdInt = Integer.parseInt(hotelId);
            final Date fromDateCast = fromDate == null || fromDate.isEmpty() ? new Date() : new Date(fromDate);
            final Date toDateCast = toDate == null || toDate.isEmpty() ? getTomorrowDate() : new Date(toDate);

            final List<Room> rooms = queryService.getRooms(hotelIdInt, fromDateCast, toDateCast, adults, children, infants);
            return ResponseEntity.ok(rooms);
        }
        catch (final NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid hotelId: " + hotelId);
        }
    }

    @GetMapping(value = "/hotels/{hotelId}")
    public ResponseEntity<Object> getHotelDetails(@PathVariable final String hotelId)
    {
        logger.info("Received request for getting hotel details for hotelId={}", hotelId);
        try
        {
            // parse params
            final int hotelIdInt = Integer.parseInt(hotelId);
            final Object hotelDetails = queryService.getHotelDetails(hotelIdInt);
            return ResponseEntity.ok(hotelDetails);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid hotelId: " + hotelId);
        }
    }

    @GetMapping(value = "/transports/{transportId}")
    public ResponseEntity<Object> getTransportDetails(@PathVariable final String transportId)
    {
        logger.info("Received request for getting transport details for transportId={}", transportId);
        try
        {
            // parse params
//            int transportIdInt = Integer.parseInt(transportId);
//            final Object transportDetails = queryService.get(transportIdInt);
            return ResponseEntity.ok("not implemented yet");
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid transportId: " + transportId);
        }
    }

    @PostMapping(value = "/reserve")
    public ResponseEntity<ReservationConfirmation> reserveTrip(@RequestBody final ReservationRequest body

    ) throws ExecutionException, InterruptedException, TimeoutException
    {
        final int hotelId = body.getHotelId();
        final int outgoingTransportId = body.getOutgoingTransportId();
        final int returnTransportId = body.getReturnTransportId();
        final int customerId = body.getCustomerId();
        final String fromDate = body.getFromDate();
        final String toDate = body.getToDate();

        final int adults = body.getAdults();
        final int children = body.getChildren();
        final int infants = body.getInfants();
        Date fromDateCast;
        Date toDateCast;
        try
        {
            fromDateCast = fromDate == null || fromDate.isEmpty() ? new Date() : new Date(fromDate);
            toDateCast = toDate == null || toDate.isEmpty() ? getTomorrowDate() : new Date(toDate);
        }
        catch (final Exception e)
        {
            logger.error("Error parsing date: ", e);
            fromDateCast = new Date();
            toDateCast = getTomorrowDate();
        }

        final Trip trip = Trip.builder()
            .withHotelId(hotelId)
            .withCustomerId(customerId)
            .withNumberOfAdults(adults)
            .withNumberOfChildren(children)
            .withNumberOfInfants(infants)
            .withOutboundDate(fromDateCast)
            .withReturnDate(toDateCast)
            .withOutboundTransportId(outgoingTransportId)
            .withReturnTransportId(returnTransportId)
            .build();

        final ReservationConfirmation reservationData = commandService.reserveTrip(trip);
        return ResponseEntity.ok(reservationData);
    }

    @PostMapping(value = "/pay")
    public ResponseEntity<Boolean> pay(@RequestBody final PaymentRequest requestBody)
    {
        final boolean paymentStatus = payForTrip(requestBody.getTripReservationId());
        return ResponseEntity.ok(paymentStatus);
    }

    public Boolean payForTrip(final int tripReservationId)
    {
        final boolean hasPaymentSucceeded = true;

        if (!hasPaymentSucceeded)
        {
            final var paymentFailedEvent = PaymentFailedEvent
                .builder().withTripReservationId(tripReservationId).build();
            return false;
        }
        else
        {
            final var paymentSucceededEvent = PaymentConfirmedEvent
                .builder().withTripReservationId(tripReservationId).build();
            eventGateway.publish(paymentSucceededEvent);
            return hasPaymentSucceeded;

        }
    }

//    @PostMapping(value = "/trips/{tripId}/reservations")
//    public ResponseEntity<Object> makeReservation(@PathVariable String tripId, @RequestBody ReservationRequest request) {
//        // Implement the logic to make a reservation for the trip with the given tripId
//        // Return the result
//        return ResponseEntity.ok("not implemented yet");
//    }

}
