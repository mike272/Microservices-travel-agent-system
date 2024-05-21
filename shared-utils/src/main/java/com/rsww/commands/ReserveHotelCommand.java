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
public class ReserveHotelCommand
{
    private int tripReservationId;
    private int hotelId;
    private int roomId;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfInfants;
    private Date checkInDate;
    private Date checkOutDate;
    private String customerName;
    private int customerId;
}
