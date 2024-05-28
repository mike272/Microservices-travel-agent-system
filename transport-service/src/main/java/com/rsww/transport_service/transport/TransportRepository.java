package com.rsww.transport_service.transport;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TransportRepository extends CrudRepository<Transport, Integer>
{
    @Query(
        "SELECT t FROM Transport t WHERE t.departureCity = :departureLocation AND t.destinationCity = :arrivalLocation AND t.departureDate >= :departureDate ORDER BY t.departureDate ASC")
    List<Transport> findDepartureFlights(@Param("departureLocation") String departureLocation,
                                         @Param("arrivalLocation") String arrivalLocation,
                                         @Param("departureDate") Date departureDate);

    @Query(
        "SELECT t FROM Transport t WHERE t.departureCity = :departureLocation AND t.destinationCity = :arrivalLocation AND t.departureDate >= :returnDate ORDER BY t.departureDate ASC")
    List<Transport> findReturnFlights(@Param("departureLocation") String departureLocation,
                                      @Param("arrivalLocation") String arrivalLocation,
                                      @Param("returnDate") Date returnDate);
}
