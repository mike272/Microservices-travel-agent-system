package com.rsww.events;

import java.util.List;

import com.rsww.dto.Hotel;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(setterPrefix = "with")
public class HotelsInitializedEvent
{
    private List<Hotel> hotels;
}
