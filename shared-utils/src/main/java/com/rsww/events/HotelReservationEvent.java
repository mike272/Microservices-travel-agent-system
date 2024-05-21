package com.rsww.events;

import java.util.Date;

import com.rsww.dto.ReservationEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Hotel reservation event. Status can be CONFIRMED, CANCELLED, CREATED
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelReservationEvent
{
    private int tripReservationId;
    private int reservationId;
    private int hotelId;
    private int customerId;
    private int roomId;
    private double amount;
    private Date checkInDate;
    private Date checkOutDate;
    private ReservationEventType status;
}
