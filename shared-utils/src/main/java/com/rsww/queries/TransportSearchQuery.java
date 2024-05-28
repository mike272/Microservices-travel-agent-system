package com.rsww.queries;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for searching available transports
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TransportSearchQuery
{
    private Date fromDate;
    private Date toDate;
    private String fromLocation;
    private String toLocation;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
}
