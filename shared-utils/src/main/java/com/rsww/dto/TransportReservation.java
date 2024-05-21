package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Transport reservation object
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TransportReservation
{
    private int clientId;
    private int transportId; // id linking to transport event (for example flight on a given date from X to Y)
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfInfants;
    private double totalPrice;
    private Date fromDate;
    private Date toDate;
    private TransportTypeEnum transportType;
}
