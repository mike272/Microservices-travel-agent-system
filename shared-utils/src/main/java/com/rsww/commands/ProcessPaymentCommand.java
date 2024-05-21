package com.rsww.commands;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand
{
    private int reservationId;
    private double amount;
    private int customerId;
}
