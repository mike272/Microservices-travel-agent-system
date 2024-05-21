package com.rsww.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This is the main object that will go to the client. Contains all information to present a trip.
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Trip
{
    //------------------ general details ------------------
    /**
     * The trip id to get the full trip information.
     * TODO:: will TA store in the database or generate new trips from hotels and transport each time?
     */
    private int tripId;
    private String tripDescription; // can be manually set
    private Double tripRating; // can be manually set
    private Double tripPrice; // sum of hotelPrice and transportPrice


    //------------------ hotel details ------------------
    /**
     * The ID of the hotel.
     */
    private int hotelId;
    private String hotelName;
    private String hotelLocation;
    private Double hotelRating;
    private Double hotelPrice; // lowest price from available rooms
    private String hotelDescription;
    private String hotelImage;

    //------------------ transport details ------------------
    /**
     * The ID of the transport.
     */
    private int transportId;
    private String transportName;
    private Date transportFromDate;
    private Date transportToDate;
    private String transportFromLocation;
    private String transportToLocation;
    private String transportTime;
    private String transportPrice;
}
