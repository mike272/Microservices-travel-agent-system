package com.rsww.queries;



import java.util.Date;

import com.rsww.dto.TransportTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for getting single transport information
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TransportEventsQuery
{
    private Date fromDate;
    private Date toDate;
    private String fromLocation;
    private String toLocation;
    private Number numberOfAdults;
    private Number numberOfChildren;
    private Number numberOfInfants;
    private TransportTypeEnum transportType;

}
