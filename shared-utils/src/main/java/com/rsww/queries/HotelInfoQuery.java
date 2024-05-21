package com.rsww.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for getting single hotel information
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelInfoQuery
{
    /**
     * The ID of the hotel.
     */
    private int hotelId;
}
