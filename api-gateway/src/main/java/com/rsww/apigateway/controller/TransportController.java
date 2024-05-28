package com.rsww.apigateway.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsww.apigateway.service.CommandService;
import com.rsww.apigateway.service.QueryService;
import com.rsww.responses.AvailableHotelsResponse;
import com.rsww.responses.AvailableTransportsResponse;

import lombok.Data;


@Data
@RestController
@CrossOrigin
@RequestMapping("/v1/transport")
public class TransportController
{
    private static final Logger logger = LoggerFactory.getLogger(TransportController.class);
    private final QueryService queryService;
    private final CommandService commandService;

    private Date getTomorrowDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    @GetMapping(value="/search")
    public AvailableTransportsResponse searchHotels(
        @RequestParam(required = false) final String fromDate,
        @RequestParam(required = false) final String toDate,
        @RequestParam(required = false) final String fromLocation,
        @RequestParam(required = false) final String toLocation,
        @RequestParam(required = false) final Integer adults,
        @RequestParam(required = false) final Integer children,
        @RequestParam(required = false) final Integer infants
    ) throws ExecutionException, InterruptedException, TimeoutException
    {
        logger.info("Received HotelSearchQuery with toLocation: {}, fromDate: {}, toDate: {}", toLocation, fromDate, toDate);
        return queryService.forwardSearchTransports(
            fromLocation,
            toLocation,
            fromDate == null || fromDate.isEmpty() ? new Date() : new Date(fromDate),
            toDate == null || toDate.isEmpty() ? getTomorrowDate() : new Date(toDate),
            adults,
            children,
            infants
        );
    }
}
