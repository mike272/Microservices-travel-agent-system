package com.rsww.events;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent
{
    private int tripReservationId;
    private int paymentId;
    private int customerId;
    private double amount;
    private String reason;
}
