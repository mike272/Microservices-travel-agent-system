package com.rsww.commands;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ReserveTransportCommand
{
    private int tripReservationId;
    // transport event should have all the transport info like dates etc so we don't have to specify it here
    private int transportEventId;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
    private String customerName;
    private int customerId;
    private Date outboundDate;
    private Date returnDate;
}
