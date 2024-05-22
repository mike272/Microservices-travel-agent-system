package com.rsww.commands;

import java.util.Date;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class ReserveTripCommand
{
    private int tripReservationId;
    // trip found by tripId should have all the data needed
    private int tripId;

    private int hotelId;
    private List<Integer> roomIds;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
    private Date checkInDate;
    private Date checkOutDate;
    private String customerName;
    private int customerId;

    private int transportId;
    private Date outboundDate;
    private Date returnDate;

    public ReserveTripCommand() {
        Random rand = new Random();
        this.tripReservationId = rand.nextInt();
    }
}
