package com.rsww.commands;

import com.rsww.dto.ReservationEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTripStatusCommand
{
    private int tripReservationId;
    private ReservationEventType status;
}
