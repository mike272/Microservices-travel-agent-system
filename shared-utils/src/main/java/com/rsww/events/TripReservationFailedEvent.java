package com.rsww.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TripReservationFailedEvent
{
    private int tripReservationId;
    private String reason;
}
