package com.rsww.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ReservationConfirmation
{
    private int reservationId;
    private int tripId;
    private int hotelId;
    private int transportId;
    private ReservationEventType status;
    private String message;
}
