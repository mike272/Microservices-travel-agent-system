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
    private int id;
    private Date departureDate;
    /*
     * do not use, for simplicity we assume that the arrival date is the same as the departure date
     */
    private Date arrivalDate;
    private int totalNumberOfSeats;
    private TransportTypeEnum transportType;
    private int period; // in days. 7=runs every week. 1=runs every day

    private String departureCity;
    private String destinationCity;
    private String departureCountry;
    private String destinationCountry;

    private int basePrice;
    private int totalPlaces;
    private int availablePlaces;
}
