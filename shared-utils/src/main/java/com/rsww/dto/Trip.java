package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This is the main object that will go to the client. Contains all information to present a trip.
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Trip
{
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
