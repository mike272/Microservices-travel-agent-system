package com.rsww.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Event to indicate that all reservations have been confirmed.
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class AllReservationsConfirmedEvent
{
    private int tripReservationId;
}
