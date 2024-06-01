package com.rsww.responses;

import java.util.ArrayList;
import java.util.List;

import com.rsww.dto.Hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for searching available hotels
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class AvailableHotelsResponse
{
    private ArrayList<Hotel> hotels;
}
