package com.rsww.travel_agent.trip;

import java.util.Date;

import com.rsww.dto.ReservationEventType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Trip {
    @Id
    @SequenceGenerator(
            name = "trip_sequence",
            sequenceName = "trip_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trip_sequence"
    )
    private int id;
    private ReservationEventType status;
    private int hotelId;
    private int outboundTransportId;
    private int returnTransportId;
    private int customerId;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfInfants;
    private double totalPrice;
    private int hotelReservationId;
    private int outboundTransportReservationId;
    private int returnTransportReservationId;
    private Date outboundDate;
    private Date returnDate;
}
