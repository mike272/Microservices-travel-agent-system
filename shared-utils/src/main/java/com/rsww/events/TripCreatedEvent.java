package com.rsww.events;

import com.rsww.dto.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TripCreatedEvent
{
    private Trip trip;
//    additional id field for saga handler
    private int tripReservationId;
}
