package com.rsww.apigateway.controller;

import lombok.Data;


@Data
public class ReservationRequest
{
    private int hotelId;
    private int outgoingTransportId;
    private int returnTransportId;
    private int customerId;
    private String fromDate;
    private String toDate;
    private int adults;
    private int children;
    private int infants;
    private String location;
}
