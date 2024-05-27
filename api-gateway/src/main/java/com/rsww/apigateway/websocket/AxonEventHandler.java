package com.rsww.apigateway.websocket;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.rsww.dto.ReservationEventType;
import com.rsww.events.AllReservationsConfirmedEvent;
import com.rsww.events.HotelReservationEvent;
import com.rsww.events.HotelsInitializedEvent;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.events.PaymentFailedEvent;
import com.rsww.events.TransportReservationEvent;
import com.rsww.events.TransportsInitializedEvent;


@Component
public class AxonEventHandler {

    private final SimpMessagingTemplate template;

    @Autowired
    public AxonEventHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventHandler
    public void on(final HotelsInitializedEvent event) {
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent(event.getHotels().size() + " hotels initialized")
            .build();
        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final TransportsInitializedEvent event) {
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent(event.getTransports().size() + " transports initialized")
            .build();
        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final PaymentFailedEvent event) {
        final Message message = Message
            .builder()
            .withType("ERROR")
            .withTextContent("Payment " + event.getPaymentId() + " failed")
            .build();
        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final PaymentConfirmedEvent event) {
        final Message message = Message
            .builder()
            .withType("SUCCESS")
            .withTextContent("Payment " + event.getPaymentId() + " succeeded")
            .build();
        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final AllReservationsConfirmedEvent event) {
        final Message message = Message
            .builder()
            .withType("SUCCESS")
            .withTextContent("Both hotel and transport reservations for trip " + event.getTripReservationId() + " have been confirmed")
            .build();
        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final HotelReservationEvent event) {
        final var messageBuilder = Message.builder();
        final var hotelPrefix = "Hotel reservation for hotel ";
        if(event.getStatus().equals(ReservationEventType.CONFIRMED)){
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(hotelPrefix + event.getHotelId() + " has been confirmed");
        } else if(event.getStatus().equals(ReservationEventType.CANCELLED)) {
            messageBuilder
                .withType("ERROR")
                .withTextContent(hotelPrefix + event.getHotelId() + " has failed");
        } else if(event.getStatus().equals(ReservationEventType.FAILED)) {
            messageBuilder
                .withType("ERROR")
                .withTextContent(hotelPrefix + event.getHotelId() + " has failed");
        }else if (event.getStatus().equals(ReservationEventType.CREATED)){
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(hotelPrefix + event.getHotelId() + " is created");
        }
        final Message message = messageBuilder.build();

        template.convertAndSend("/send", message);
    }

    @EventHandler
    public void on(final TransportReservationEvent event){
        final var messageBuilder = Message.builder();
        final var transportPrefix = "Transport reservation for transport ";
        if(event.getStatus().equals(ReservationEventType.CONFIRMED)){
            messageBuilder
                .withType("SUCCESS")
                .withTextContent( transportPrefix  + event.getTripReservationId() + " has been confirmed");
        } else if(event.getStatus().equals(ReservationEventType.CANCELLED)) {
            messageBuilder
                .withType("ERROR")
                .withTextContent(transportPrefix + event.getTripReservationId() + " has failed");
        } else if(event.getStatus().equals(ReservationEventType.FAILED)) {
            messageBuilder
                .withType("ERROR")
                .withTextContent(transportPrefix + event.getTripReservationId() + " has failed");
        }else if (event.getStatus().equals(ReservationEventType.CREATED)){
            messageBuilder
                .withType("SUCCESS")
                .withTextContent(transportPrefix + event.getTripReservationId() + " is created");
        }
        final Message message = messageBuilder.build();

        template.convertAndSend("/send", message);
    }



}