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
    private int outboundTransportId;
    private int returnTransportId;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
    private int customerId;
    private Date outboundDate;
    private Date returnDate;
}
