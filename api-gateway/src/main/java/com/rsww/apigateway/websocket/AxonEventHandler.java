package com.rsww.apigateway.websocket;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.rsww.dto.ReservationConfirmation;
import com.rsww.dto.ReservationEventType;
import com.rsww.events.AllReservationsConfirmedEvent;
import com.rsww.events.AllReservationsCreatedEvent;
import com.rsww.events.HotelReservationEvent;
import com.rsww.events.HotelsInitializedEvent;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.events.PaymentFailedEvent;
import com.rsww.events.RoomsInitializedEvent;
import com.rsww.events.TransportReservationEvent;
import com.rsww.events.TransportsInitializedEvent;
import com.rsww.events.TripReservationFailedEvent;


@Component
public class AxonEventHandler
{
    private final String TOPIC_NAME = "/topic/messages";
    private final SimpMessagingTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(AxonEventHandler.class);

    @Autowired
    public AxonEventHandler(final SimpMessagingTemplate template)
    {
        this.template = template;
    }

    @EventHandler
    public void on(final HotelsInitializedEvent event)
    {
        logger.info("Received event: HotelsInitializedEvent");
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent(event.getHotels().size() + " hotels initialized")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final RoomsInitializedEvent event)
    {
        logger.info("Received event: RoomsInitializedEvent");
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent(event.getNumberOfRooms() + " rooms initialized")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final TransportsInitializedEvent event)
    {
        logger.info("Received event: TransportsInitializedEvent");
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent(event.getTransports().size() + " transports initialized")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final PaymentFailedEvent event)
    {
        final Message message = Message
            .builder()
            .withType("ERROR")
            .withTextContent("Payment " + event.getPaymentId() + " failed")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final PaymentConfirmedEvent event)
    {
        final Message message = Message
            .builder()
            .withType("SUCCESS")
//            .withStatus(ReservationEventType.CONFIRMED)
            .withTextContent("Payment " + event.getPaymentId() + " succeeded")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final AllReservationsConfirmedEvent event)
    {
        final Message message = Message
            .builder()
            .withTripReservationId(event.getTripReservationId())
            .withCustomerId(event.getCustomerId())
            .withType("SUCCESS")
            .withStatus(ReservationEventType.CONFIRMED)
            .withTextContent("Both hotel and transport reservations for trip " + event.getTripReservationId() + " to " + event.getLocation() + " on "
                + event.getDates() + " have been confirmed")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final AllReservationsCreatedEvent event)
    {
        final Message message = Message
            .builder()
            .withTripReservationId(event.getTripReservationId())
            .withCustomerId(event.getCustomerId())
            .withType("SUCCESS")
            .withStatus(ReservationEventType.CREATED)
            .withTextContent("Both hotel and transport reservations for trip " + event.getTripReservationId() + " to " + event.getLocation() + " on "
                + event.getDates() + " have been created.You can pay now")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final HotelReservationEvent event)
    {
        final var messageBuilder = Message.builder();
        final var hotelPrefix = "Hotel reservation for hotel ";
        if (event.getStatus().equals(ReservationEventType.CONFIRMED))
        {
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(hotelPrefix + event.getHotelId() + " in " + event.getLocation() + " on "
                    + event.getDates() + " has been confirmed");
        }
        else if (event.getStatus().equals(ReservationEventType.CANCELLED))
        {
            messageBuilder
                .withType("ERROR")
                .withTextContent(hotelPrefix + event.getHotelId() + " has failed");
        }
        else if (event.getStatus().equals(ReservationEventType.FAILED))
        {
            messageBuilder
                .withType("ERROR")
                .withTextContent(hotelPrefix + event.getHotelId() + " has failed");
        }
        else if (event.getStatus().equals(ReservationEventType.CREATED))
        {
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(hotelPrefix + event.getHotelId() + " in " + event.getLocation() + " on "
                    + event.getDates() + " is created");
        }
        final Message message = messageBuilder.build();
        final var x = 1;
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final TripReservationFailedEvent event)
    {
        final Message message = Message
            .builder()
            .withType("ERROR")
            .withTextContent("Trip reservation " + event.getTripReservationId() + " failed. Reason: " + event.getReason())
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final ReservationConfirmation event)
    {
        final Message message = Message
            .builder()
            .withType("SUCCESS")
            .withTextContent("Trip " + event.getTripId() + event.getMessage() + " has been created")
            .build();
        template.convertAndSend(TOPIC_NAME, message);
    }

    @EventHandler
    public void on(final TransportReservationEvent event)
    {
        final var messageBuilder = Message.builder();
        final var transportPrefix = "Transport reservation for transport ";
        if (event.getStatus().equals(ReservationEventType.CONFIRMED))
        {
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(transportPrefix + event.getTripReservationId() + " to " + event.getLocation() + " on "
                    + event.getDates() + " has been confirmed");
        }
        else if (event.getStatus().equals(ReservationEventType.CANCELLED))
        {
            messageBuilder
                .withType("ERROR")
                .withTextContent(transportPrefix + event.getTripReservationId() + " has failed");
        }
        else if (event.getStatus().equals(ReservationEventType.FAILED))
        {
            messageBuilder
                .withType("ERROR")
                .withTextContent(transportPrefix + event.getTripReservationId() + " has failed");
        }
        else if (event.getStatus().equals(ReservationEventType.CREATED))
        {
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(transportPrefix + event.getTripReservationId() + " to " + event.getLocation() + " on "
                    + event.getDates() + " is created");
        }
        final Message message = messageBuilder.build();

        template.convertAndSend(TOPIC_NAME, message);
    }

}