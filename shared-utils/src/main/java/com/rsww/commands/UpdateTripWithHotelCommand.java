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
public class UpdateTripWithHotelCommand
{
    private int tripId;
    private int hotelId;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;
}
