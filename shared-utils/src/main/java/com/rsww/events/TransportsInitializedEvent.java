package com.rsww.events;

import java.util.List;

import com.rsww.dto.Transport;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(setterPrefix = "with")
public class TransportsInitializedEvent
{
    private List<Transport> transports;
}
