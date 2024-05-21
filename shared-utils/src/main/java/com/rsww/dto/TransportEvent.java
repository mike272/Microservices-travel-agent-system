package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for getting transport events available for given filter
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TransportEvent
{
    private int transportId; // links to scheduled transport details
    private String transportType; // type of transport
    private Date departureDate;
    private Date arrivalDate;
    private int availableSeats;
    private double pricePerSeat;
    private String departureLocation;
    private String arrivalLocation;
}
