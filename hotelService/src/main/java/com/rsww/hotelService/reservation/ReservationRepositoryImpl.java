package com.rsww.hotelService.reservation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository
{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reservation> getReservationsByRoomId(final int roomId)
    {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.room.id = :roomId", Reservation.class)
            .setParameter("roomId", roomId)
            .getResultList();
    }

    @Override
    public List<Reservation> findReservationsByRoomIdAndDateRange(final int roomId, final Date fromDate, final Date toDate) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND ((r.fromDate BETWEEN :fromDate AND :toDate) OR (r.toDate BETWEEN :fromDate AND :toDate))", Reservation.class)
            .setParameter("roomId", roomId)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .getResultList();
    }

    @Override
    public <S extends Reservation> S save(final S entity)
    {
        return null;
    }

    @Override
    public <S extends Reservation> Iterable<S> saveAll(final Iterable<S> entities)
    {
        return null;
    }

    @Override
    public Optional<Reservation> findById(final Integer aInteger)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Integer aInteger)
    {
        return false;
    }

    @Override
    public Iterable<Reservation> findAll()
    {
        return null;
    }

    @Override
    public Iterable<Reservation> findAllById(final Iterable<Integer> Integers)
    {
        return null;
    }

    @Override
    public long count()
    {
        return 0;
    }

    @Override
    public void deleteById(final Integer aInteger)
    {

    }

    @Override
    public void delete(final Reservation entity)
    {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Integer> Integers)
    {

    }

    @Override
    public void deleteAll(final Iterable<? extends Reservation> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }
}
