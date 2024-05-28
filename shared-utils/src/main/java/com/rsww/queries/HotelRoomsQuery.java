package com.rsww.queries;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for getting all rooms available for given filter
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelRoomsQuery
{
    private int hotelId;
    private Date checkInDate;
    private Date checkOutDate;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
}
