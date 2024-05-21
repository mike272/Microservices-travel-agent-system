package com.rsww.events;


import com.rsww.dto.ReservationEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Transport reservation event. Status can be CONFIRMED, CANCELLED, CREATED
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TransportReservationEvent
{
    private int tripReservationId;
    private int transportEventId;
    private int customerId;
    private double amount;
    private ReservationEventType status;
}
