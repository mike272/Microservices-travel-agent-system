package com.rsww.responses;

import com.rsww.dto.Hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for getting a single hotel details
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailsResponse
{
    private Hotel hotel;
}
