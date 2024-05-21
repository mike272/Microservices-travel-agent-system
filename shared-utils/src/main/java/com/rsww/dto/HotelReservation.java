package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Hotel reservation object
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelReservation
{
    private int clientId;
    private int hotelId;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfInfants;
    private double totalPrice;
    private Date fromDate;
    private Date toDate;
    private ReservationEventType reservationStatus;
}
