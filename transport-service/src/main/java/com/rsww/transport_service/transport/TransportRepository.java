package com.rsww.transport_service.transport;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TransportRepository extends CrudRepository<Transport, Integer>
{

    @Query("SELECT t FROM Transport t LEFT JOIN Reservation r ON t.id = r.transport.id " +
        "WHERE t.departureLocation = :departureLocation AND t.arrivalLocation = :arrivalLocation " +
        "AND t.departureDate = :departureDate " +
        "GROUP BY t.id " +
        "HAVING (t.totalPlaces - COALESCE(SUM(r.reservedPlaces), 0)) >= :numberOfPeople")
    List<Transport> findAvailableTransports(@Param("departureLocation") String departureLocation,
                                            @Param("arrivalLocation") String arrivalLocation,
                                            @Param("departureDate") Date departureDate,
                                            @Param("numberOfPeople") Integer numberOfPeople);
}
