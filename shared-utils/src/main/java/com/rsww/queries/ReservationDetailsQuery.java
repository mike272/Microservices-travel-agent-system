package com.rsww.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for getting single reservation details

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailsQuery
{
    private int reservationId;
}
