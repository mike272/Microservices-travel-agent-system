package com.rsww.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Hotel object
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Hotel
{
    private int id;

    private String name;
    private String city;
    private String country;
    private int starsNum;
    private boolean areChildrenAllowed;
    private String description;
    private String location;
    private Double minPrice;
}
