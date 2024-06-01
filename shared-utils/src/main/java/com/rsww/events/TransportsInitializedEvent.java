package com.rsww.events;

import java.util.ArrayList;

import com.rsww.dto.Transport;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(setterPrefix = "with")
public class TransportsInitializedEvent
{
    private ArrayList<Transport> transports;
}
