package com.rsww.apigateway.websocket;

import com.rsww.dto.ReservationEventType;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(setterPrefix = "with")
public class Message
{
    private String textContent;
    private String type;
    private int tripReservationId;
    private ReservationEventType status;
}
