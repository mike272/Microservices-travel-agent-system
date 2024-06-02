package com.rsww.responses;

import java.util.ArrayList;

import com.rsww.dto.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for searching trips for selected user
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class UsersTripsResponse
{
    private ArrayList<Trip> trips;

}
