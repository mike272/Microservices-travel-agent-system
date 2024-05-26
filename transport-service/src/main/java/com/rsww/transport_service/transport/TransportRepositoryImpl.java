package com.rsww.transport_service.transport;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


public class TransportRepositoryImpl implements TransportRepository
{
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Transport> findAvailableTransports(final String departureLocation,
                                                   final String arrivalLocation,
                                                   final Date departureDate,
                                                   final Integer numberOfPeople)
    {
        return List.of();
    }

    @Override
    public <S extends Transport> S save(final S entity)
    {
        return null;
    }

    @Override
    public <S extends Transport> Iterable<S> saveAll(final Iterable<S> entities)
    {
        entities.forEach(e ->
            entityManager.persist(
                Transport.builder()
                    .withTransportType(e.getTransportType())
                    .withDepartureDate(e.getDepartureDate())
                    .withBasePrice(e.getBasePrice())
                    .withDepartureCity(e.getDepartureCity())
                    .withAvailablePlaces(e.getAvailablePlaces())
                    .withDestinationCity(e.getDestinationCity())
                    .build()
            ));
        return null;
    }

    @Override
    public Optional<Transport> findById(final Integer integer)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Integer integer)
    {
        return false;
    }

    @Override
    public Iterable<Transport> findAll()
    {
        return null;
    }

    @Override
    public Iterable<Transport> findAllById(final Iterable<Integer> integers)
    {
        return null;
    }

    @Override
    public long count()
    {
        return 0;
    }

    @Override
    public void deleteById(final Integer integer)
    {

    }

    @Override
    public void delete(final Transport entity)
    {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Integer> integers)
    {

    }

    @Override
    public void deleteAll(final Iterable<? extends Transport> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }
}
