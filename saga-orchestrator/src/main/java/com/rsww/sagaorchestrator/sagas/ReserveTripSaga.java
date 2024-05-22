package com.rsww.sagaorchestrator.sagas;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.rsww.commands.ReserveHotelCommand;
import com.rsww.commands.ReserveTransportCommand;
import com.rsww.commands.ReserveTripCommand;
import com.rsww.dto.ReservationEventType;
import com.rsww.events.AllReservationsConfirmedEvent;
import com.rsww.events.HotelReservationEvent;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.events.TransportReservationEvent;
import com.rsww.events.TripReservationFailedEvent;

import lombok.extern.slf4j.Slf4j;


@Saga
@Slf4j
public class ReserveTripSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient EventGateway eventGateway;

    private ReservationEventType hotelStatus;
    private ReservationEventType transportStatus;


    @StartSaga
    @SagaEventHandler(associationProperty = "reservationId")
    public void handle(final ReserveTripCommand command) {
//        this is start of saga. It is triggered by frontend and results in reserving transport and hotel
        final var hotelCommand = ReserveHotelCommand.builder()
            .withTripReservationId(command.getTripReservationId())
            .withCustomerId(command.getCustomerId())
            .withHotelId(command.getHotelId())
            .withRoomIds(command.getRoomIds())
            .withCheckInDate(command.getCheckInDate())
            .withCheckOutDate(command.getCheckOutDate())
            .withNumOfAdults(command.getNumOfAdults())
            .withNumOfChildren(command.getNumOfChildren())
            .withNumOfInfants(command.getNumOfInfants())
            .build();

        commandGateway.send(hotelCommand);

        final var transportCommand = ReserveTransportCommand.builder()
            .withTripReservationId(command.getTripReservationId())
            .withCustomerId(command.getCustomerId())
            .withCustomerName(command.getCustomerName())
            .withTransportEventId(command.getTransportId())
            .withOutboundDate(command.getOutboundDate())
            .withReturnDate(command.getReturnDate())
            .withNumOfAdults(command.getNumOfAdults())
            .withNumOfChildren(command.getNumOfChildren())
            .withNumOfInfants(command.getNumOfInfants())
            .build();
        commandGateway.send(transportCommand);
    }

    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final HotelReservationEvent event) {
        if (event.getStatus() == ReservationEventType.FAILED) {
            hotelStatus = ReservationEventType.FAILED;

            final var transportRollbackReservationCommand = TransportReservationEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withStatus(ReservationEventType.FAILED)
                .build();
            final var tripreservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Hotel reservation failed")
                .build();

            eventGateway.publish(tripreservationFailedEvent);
            commandGateway.send(transportRollbackReservationCommand);
        } else if(event.getStatus() == ReservationEventType.CANCELLED){
            hotelStatus = ReservationEventType.CANCELLED;

            final var transportRollbackReservationCommand = TransportReservationEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withStatus(ReservationEventType.CANCELLED)
                .build();
            commandGateway.send(transportRollbackReservationCommand);
        }else if(event.getStatus() == ReservationEventType.CREATED){
            hotelStatus = ReservationEventType.CONFIRMED;
        }else if (event.getStatus() == ReservationEventType.CONFIRMED){
            hotelStatus = ReservationEventType.CONFIRMED;
            if(transportStatus == ReservationEventType.CONFIRMED){
                final var allReservationsConfirmedEvent = AllReservationsConfirmedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                eventGateway.publish(allReservationsConfirmedEvent);
                SagaLifecycle.end();
            }
        }
    }

    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final TransportReservationEvent event) {
        if (event.getStatus() == ReservationEventType.FAILED) {
            transportStatus = ReservationEventType.FAILED;

            final var hotelRollbackReservationCommand = HotelReservationEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withStatus(ReservationEventType.FAILED)
                .build();
            final var tripreservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Transport reservation failed")
                .build();

            eventGateway.publish(tripreservationFailedEvent);
            commandGateway.send(hotelRollbackReservationCommand);
        } else if(event.getStatus() == ReservationEventType.CANCELLED){
            transportStatus = ReservationEventType.CANCELLED;

            final var hotelRollbackReservationCommand = HotelReservationEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withStatus(ReservationEventType.CANCELLED)
                .build();
            commandGateway.send(hotelRollbackReservationCommand);
        }else if(event.getStatus() == ReservationEventType.CREATED){
            transportStatus = ReservationEventType.CONFIRMED;
        }else if (event.getStatus() == ReservationEventType.CONFIRMED){
            transportStatus = ReservationEventType.CONFIRMED;
            if(hotelStatus == ReservationEventType.CONFIRMED){
                final var allReservationsConfirmedEvent = AllReservationsConfirmedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                eventGateway.publish(allReservationsConfirmedEvent);
                SagaLifecycle.end();
            }

        }
    }

//    @EndSaga
    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final PaymentConfirmedEvent event){
        log.info("Payment confirmed for trip reservation {}", event.getTripReservationId());
        final var confirmHotelReservationCommand = HotelReservationEvent.builder()
            .withTripReservationId(event.getTripReservationId())
            .withStatus(ReservationEventType.CONFIRMED)
            .build();
        final var confirmTransportReservationCommand = TransportReservationEvent.builder()
            .withTripReservationId(event.getTripReservationId())
            .withStatus(ReservationEventType.CONFIRMED)
            .build();
        commandGateway.send(confirmTransportReservationCommand);
        commandGateway.send(confirmHotelReservationCommand);


        //        if(hotelStatus == ReservationEventType.CREATED && transportStatus == ReservationEventType.CREATED){
//            final var allReservationsConfirmedEvent = AllReservationsConfirmedEvent.builder()
//                .withTripReservationId(event.getTripReservationId())
//                .build();
//            eventGateway.publish(allReservationsConfirmedEvent);
//        }else{
//            final var tripreservationFailedEvent = TripReservationFailedEvent.builder()
//                .withTripReservationId(event.getTripReservationId())
//                .withReason("Payment failed. One of the reservations was no longer reserved.")
//                .build();
//            eventGateway.publish(tripreservationFailedEvent);
//        }
    }
}