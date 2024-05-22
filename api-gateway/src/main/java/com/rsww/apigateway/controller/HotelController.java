package com.rsww.apigateway.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsww.apigateway.service.CommandService;
import com.rsww.apigateway.service.QueryService;
import com.rsww.dto.Hotel;
import com.rsww.responses.AvailableHotelsResponse;

import lombok.Data;


@Data
@RestController
@CrossOrigin
@RequestMapping("/v1/hotels")
public class HotelController
{
    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private final QueryService queryService;
    private final CommandService commandService;

    @GetMapping(value="/search")
    public AvailableHotelsResponse searchHotels(
        @RequestParam(required = false) final String fromDate,
        @RequestParam(required = false) final String toDate,
        @RequestParam(required = false) final String toLocation
    ) throws ExecutionException, InterruptedException, TimeoutException
    {
        logger.info("Received HotelSearchQuery with toLocation: {}, fromDate: {}, toDate: {}", toLocation, fromDate, toDate);
        return queryService.forwardSearchHotels(
            toLocation,
            fromDate == null || fromDate.isEmpty() ? null : new Date(fromDate),
            toDate == null || toDate.isEmpty() ? null : new Date(toDate)
        );
    }

    @PostMapping(value="/initialize")
    public ResponseEntity<Boolean> initializeHotels()
    {
        commandService.initializeHotels();
        return ResponseEntity.ok(true);
    }
}
