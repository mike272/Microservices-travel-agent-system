package com.rsww.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a query for trip details.
 * It contains a single field, tripId, which is used to identify the trip, and returns details about hotel and transport for given trip.
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailsQuery
{
    /**
     * The ID of the trip.
     */
    private int tripId;
}
