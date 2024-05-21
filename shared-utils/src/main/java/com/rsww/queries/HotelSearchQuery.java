package com.rsww.queries;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for searching available hotels
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchQuery
{
    private Date fromDate;
    private Date toDate;
    private String fromLocation;
    private String toLocation;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
}
