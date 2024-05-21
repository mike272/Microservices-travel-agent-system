package com.rsww.commands;


import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class InitiateTripWithTransportCommand
{
    private int tripReservationId;
    private int transportId;
    private int customerId;
    private String customerName;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;

    public InitiateTripWithTransportCommand() {
        Random rand = new Random();
        this.tripReservationId = rand.nextInt();
    }
}
