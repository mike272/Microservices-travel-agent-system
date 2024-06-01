package com.rsww.responses;

import java.util.ArrayList;
import java.util.List;

import com.rsww.dto.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for searching trips
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class TripSearchResponse
{
    private ArrayList<Trip> trips;
}
