package com.rsww.hotelService.room;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Repository
public class RoomRepositoryImpl implements RoomRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Room> findRoomsByNumberOfGuests(final int numberOfAdults, final int numberOfChildren, final int numberOfInfants)
    {
        int totalGuests = numberOfAdults + numberOfChildren + numberOfInfants;
        return entityManager.createQuery("SELECT r FROM Room r WHERE r.numberOfPeople >= :totalGuests", Room.class)
            .setParameter("totalGuests", totalGuests)
            .getResultList();

//            findAll().stream()
//            .filter(room -> room.getNumberOfPeople() >= totalGuests)
//            .collect(Collectors.toList());
    }

    @Override
    public <S extends Room> S save(final S entity)
    {
        return null;
    }

    @Override
    public <S extends Room> List<S> saveAll(final Iterable<S> entities)
    {
        return null;
    }

    @Override
    public Optional<Room> findById(final Integer integer)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Integer integer)
    {
        return false;
    }

    @Override
    @Transactional
    public List<Room> findAll()
    {
        return entityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    @Override
    public Iterable<Room> findAllById(final Iterable<Integer> integers)
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
    public void delete(final Room entity)
    {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Integer> integers)
    {

    }

    @Override
    public void deleteAll(final Iterable<? extends Room> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }
}
