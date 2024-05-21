package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a scheduled regular transport event. Occurs every n days.
 * refer to TransportEvent for one-time events
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Transport
{
    private int id;
    private Date departureDate;
    private Date arrivalDate;
    private int totalNumberOfSeats;
    private double pricePerSeat;
    private String departureLocation;
    private String arrivalLocation;
    private TransportTypeEnum transportType;
    private int period; // in days. 7=runs every week. 1=runs every day
}
