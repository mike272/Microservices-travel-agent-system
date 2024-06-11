package com.rsww.sagaorchestrator.sagas;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rsww.commands.CancelHotelReservationCommand;
import com.rsww.commands.CancelReservationCommand;
import com.rsww.commands.CancelTransportReservationCommand;
import com.rsww.commands.ConfirmHotelReservationCommand;
import com.rsww.commands.ConfirmTransportReservationCommand;
import com.rsww.commands.ReserveHotelCommand;
import com.rsww.commands.ReserveTransportCommand;
import com.rsww.commands.ReserveTripCommand;
import com.rsww.commands.UpdateTripStatusCommand;
import com.rsww.dto.ReservationEventType;
import com.rsww.events.AllReservationsConfirmedEvent;
import com.rsww.events.AllReservationsCreatedEvent;
import com.rsww.events.HotelReservationEvent;
import com.rsww.events.PaymentConfirmedEvent;
import com.rsww.events.PaymentFailedEvent;
import com.rsww.events.TransportReservationEvent;
import com.rsww.events.TripCreatedEvent;
import com.rsww.events.TripReservationFailedEvent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Saga
@Slf4j
@NoArgsConstructor
public class ReserveTripSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient EventGateway eventGateway;

//    @Autowired
//    public ReserveTripSaga(final CommandGateway commandGateway, final EventGateway eventGateway) {
//        this.commandGateway = commandGateway;
//        this.eventGateway = eventGateway;
//    }

    private ReservationEventType hotelStatus;
    private ReservationEventType transportStatus;
    private int customerId;

    @StartSaga
    @SagaEventHandler(associationProperty = "tripReservationId")
    public void handle(final TripCreatedEvent command) {
        final var receivedTripData = command.getTrip();
//        this is start of saga. It is triggered by frontend and results in reserving transport and hotel
        SagaLifecycle.associateWith("tripReservationId", receivedTripData.getId());
        customerId = event.getCustomerId();
        final var hotelCommand = ReserveHotelCommand.builder()
            .withTripReservationId(receivedTripData.getId())
            .withCustomerId(receivedTripData.getCustomerId())
            .withHotelId(receivedTripData.getHotelId())
//            .withRoomIds(receivedTripData.getRoomIds())
            .withCheckInDate(receivedTripData.getOutboundDate())
            .withCheckOutDate(receivedTripData.getReturnDate())
            .withNumOfAdults(receivedTripData.getNumberOfAdults())
            .withNumOfChildren(receivedTripData.getNumberOfChildren())
            .withNumOfInfants(receivedTripData.getNumberOfInfants())
            .build();

        commandGateway.send(hotelCommand);

        final var transportCommand = ReserveTransportCommand.builder()
            .withTripReservationId(receivedTripData.getId())
            .withCustomerId(receivedTripData.getCustomerId())
            .withOutboundTransportId(receivedTripData.getOutboundTransportId())
            .withReturnTransportId(receivedTripData.getReturnTransportId())
            .withOutboundDate(receivedTripData.getOutboundDate())
            .withReturnDate(receivedTripData.getReturnDate())
            .withNumOfAdults(receivedTripData.getNumberOfAdults())
            .withNumOfChildren(receivedTripData.getNumberOfChildren())
            .withNumOfInfants(receivedTripData.getNumberOfInfants())
            .build();

        commandGateway.send(transportCommand);
    }

    @SagaEventHandler(keyName = "tripReservationId", associationProperty = "tripReservationId")
    public void on(final HotelReservationEvent event) {
        if (event.getStatus() == ReservationEventType.FAILED) {
            hotelStatus = ReservationEventType.FAILED;

            if(transportStatus !=null && transportStatus.equals(ReservationEventType.FAILED)){
                final var cancelTransportReservation = CancelTransportReservationCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                commandGateway.send(cancelTransportReservation);
            }

            final var cancelTripReservation = CancelReservationCommand.builder()
                .withTripReservationId(event.getTripReservationId())
                .build();

            final var tripReservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Hotel reservation failed")
                .build();

            eventGateway.publish(tripReservationFailedEvent);
            commandGateway.send(cancelTripReservation);
        } else if(event.getStatus() == ReservationEventType.CANCELLED){
            hotelStatus = ReservationEventType.CANCELLED;

            if(transportStatus !=null && !transportStatus.equals(ReservationEventType.CANCELLED)){
                final var cancelTransportReservation = CancelTransportReservationCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                commandGateway.send(cancelTransportReservation);
            }

            final var cancelTripReservation = CancelReservationCommand.builder()
                .withTripReservationId(event.getTripReservationId())
                .build();

            final var tripReservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Hotel reservation cancelled")
                .build();

            eventGateway.publish(tripReservationFailedEvent);
            commandGateway.send(cancelTripReservation);
        }else if(event.getStatus() == ReservationEventType.CREATED){
            hotelStatus = ReservationEventType.CREATED;
            if(transportStatus == ReservationEventType.CREATED)
            {
                final AllReservationsCreatedEvent allReservationsCreatedEvent = AllReservationsCreatedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withLocation(event.getLocation())
                    .withDates(event.getDates())
                    .withCustomerId(customerId)
                    .build();
                final UpdateTripStatusCommand updateTripStatus = UpdateTripStatusCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withStatus(ReservationEventType.CREATED)
                    .build();

                commandGateway.send(updateTripStatus);
                eventGateway.publish(allReservationsCreatedEvent);
            }
        }else if (event.getStatus() == ReservationEventType.CONFIRMED){
            hotelStatus = ReservationEventType.CONFIRMED;
            if(transportStatus == ReservationEventType.CONFIRMED){
                final var allReservationsConfirmedEvent = AllReservationsConfirmedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withLocation(event.getLocation())
                    .withDates(event.getDates())
                    .withCustomerId(customerId)
                    .build();
                final var updateTripStatus = UpdateTripStatusCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withStatus(ReservationEventType.CONFIRMED)
                    .build();

                commandGateway.send(updateTripStatus);
                eventGateway.publish(allReservationsConfirmedEvent);
                SagaLifecycle.end();
            }
        }
    }

    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final TransportReservationEvent event) {
        if (event.getStatus() == ReservationEventType.FAILED) {
            transportStatus = ReservationEventType.FAILED;
            if(hotelStatus !=null && !hotelStatus.equals(ReservationEventType.FAILED)){
                final var cancelHotelReservation = CancelHotelReservationCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                commandGateway.send(cancelHotelReservation);
            }


            final var cancelTripReservation = CancelReservationCommand.builder()
                .withTripReservationId(event.getTripReservationId())
                .build();

            final var tripReservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Transport reservation failed")
                .build();

            eventGateway.publish(tripReservationFailedEvent);
            commandGateway.send(cancelTripReservation);
        } else if(event.getStatus() == ReservationEventType.CANCELLED){
            transportStatus = ReservationEventType.CANCELLED;
            if(hotelStatus!=null &&!hotelStatus.equals(ReservationEventType.CANCELLED)){
                final var cancelHotelReservation = CancelHotelReservationCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .build();
                commandGateway.send(cancelHotelReservation);
            }

            final var cancelTripReservation = CancelReservationCommand.builder()
                .withTripReservationId(event.getTripReservationId())
                .build();

            final var tripReservationFailedEvent = TripReservationFailedEvent.builder()
                .withTripReservationId(event.getTripReservationId())
                .withReason("Transport reservation cancelled")
                .build();

            eventGateway.publish(tripReservationFailedEvent);
            commandGateway.send(cancelTripReservation);
        }else if(event.getStatus() == ReservationEventType.CREATED){
            transportStatus = ReservationEventType.CREATED;
            if(hotelStatus == ReservationEventType.CREATED){
                final var allReservationsCreatedEvent = AllReservationsCreatedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withLocation(event.getLocation())
                    .withDates(event.getDates())
                    .withCustomerId(customerId)
                    .build();
                final var updateTripStatus = UpdateTripStatusCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withStatus(ReservationEventType.CREATED)
                    .build();

                commandGateway.send(updateTripStatus);
                eventGateway.publish(allReservationsCreatedEvent);
            }
        }else if (event.getStatus() == ReservationEventType.CONFIRMED){
            transportStatus = ReservationEventType.CONFIRMED;
            if(hotelStatus == ReservationEventType.CONFIRMED){
                final var allReservationsConfirmedEvent = AllReservationsConfirmedEvent.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withLocation(event.getLocation())
                    .withCustomerId(customerId)
                    .withDates(event.getDates())
                    .build();

                final var updateTripStatus = UpdateTripStatusCommand.builder()
                    .withTripReservationId(event.getTripReservationId())
                    .withStatus(ReservationEventType.CONFIRMED)
                    .build();

                commandGateway.send(updateTripStatus);
                eventGateway.publish(allReservationsConfirmedEvent);
                SagaLifecycle.end();
            }

        }
    }

    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final PaymentConfirmedEvent event){
        log.info("Payment confirmed for trip reservation {}", event.getTripReservationId());

        final var confirmHotelReservationCommand = ConfirmHotelReservationCommand.builder()
            .withTripReservationId(event.getTripReservationId())
            .build();
        final var confirmTransportReservationCommand = ConfirmTransportReservationCommand.builder()
            .withTripReservationId(event.getTripReservationId())
            .build();

        commandGateway.send(confirmTransportReservationCommand);
        commandGateway.send(confirmHotelReservationCommand);
    }

    @SagaEventHandler(associationProperty = "tripReservationId")
    public void on(final PaymentFailedEvent event){
        log.info("Payment failed for trip reservation {}", event.getTripReservationId());

        final var cancelHotelReservationCommand = CancelHotelReservationCommand.builder()
            .withTripReservationId(event.getTripReservationId())
            .build();
        final var cancelTransportReservationCommand = CancelTransportReservationCommand.builder()
            .withTripReservationId(event.getTripReservationId())
            .build();
        final var cancelTripReservationCommand = CancelReservationCommand.builder()
            .withTripReservationId(event.getTripReservationId())
            .build();

        commandGateway.send(cancelHotelReservationCommand);
        commandGateway.send(cancelTransportReservationCommand);
        commandGateway.send(cancelTripReservationCommand);
    }
}