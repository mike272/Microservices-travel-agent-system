package com.rsww.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a room in a hotel
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Room
{
    private int id;
    private int hotelId;
    private int numberOfPeople;
    private double basePrice;
}
