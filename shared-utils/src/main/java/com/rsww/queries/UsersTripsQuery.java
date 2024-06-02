package com.rsww.queries;

import java.util.ArrayList;

import com.rsww.dto.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Query object for getting trips for a user
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class UsersTripsQuery
{
    private int customerId;
}
