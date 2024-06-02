package com.rsww.travel_agent.trip;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TripRepository extends JpaRepository<Trip, Integer>
{
    @Query(
        "SELECT t FROM Trip t WHERE t.customerId=:customerId"
    )
    List<Trip> getTripsforUser(@Param("customerId")  int customerId);
}
