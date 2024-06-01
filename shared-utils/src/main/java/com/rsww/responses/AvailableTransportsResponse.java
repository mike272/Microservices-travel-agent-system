package com.rsww.responses;

import java.util.ArrayList;
import java.util.List;

import com.rsww.dto.Transport;
import com.rsww.dto.TransportEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response object for getting all rooms available for given filter
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTransportsResponse
{
    private ArrayList<Transport> transports;
}
