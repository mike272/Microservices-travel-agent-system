package com.rsww.transport_service.transport;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


public interface TransportRepository extends CrudRepository<Transport, Integer>
{
    @Query(
        "SELECT t FROM Transport t WHERE "
            + "(LOWER(t.departureCity) = LOWER(:departureLocation) OR LOWER(t.departureCountry) = LOWER(:departureLocation)) AND "
            + "(LOWER(t.destinationCity) = LOWER(:arrivalLocation) OR LOWER(t.destinationCountry) = LOWER(:arrivalLocation)) AND "
            + "t.departureDate >= :departureDate")
    List<Transport> findFlights(@Param("departureLocation") String departureLocation,
                                @Param("arrivalLocation") String arrivalLocation,
                                @Param("departureDate") Date departureDate,
                                Pageable pageable);

}