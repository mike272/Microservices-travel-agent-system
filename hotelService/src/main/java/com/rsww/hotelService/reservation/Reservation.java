package com.rsww.hotelService.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.rsww.dto.ReservationEventType;
import com.rsww.hotelService.hotel.Hotel;
import com.rsww.hotelService.room.Room;


@Data
@Entity
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private int roomId;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    private int hotelId;

    private int clientId;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfInfants;
    private double totalPrice;
    private Date fromDate;
    private Date toDate;
    private ReservationEventType reservationStatus;

//
//    public void addEvent(final HotelReservationEvent event) {
//        this.events.add(event);
//    }
//
//    public void applyEvents() {
//        for (final HotelReservationEvent event : this.events) {
//            switch (event.getStatus()) {
//                case CREATED:
//                    this.setReservationStatus(ReservationEventType.CREATED);
////                    this.setNumberOfAdults(event.getEventData());
//                    break;
//                case CANCELLED:
//                    // apply the logic when the reservation is cancelled
//                    break;
//                case CONFIRMED:
//                    // apply the logic when the reservation is confirmed
//                    break;
//                // add more cases if there are more event types
//                default:
//                    throw new IllegalArgumentException("Unknown event type: " + event.getStatus());
//            }
//        }
//    }

}