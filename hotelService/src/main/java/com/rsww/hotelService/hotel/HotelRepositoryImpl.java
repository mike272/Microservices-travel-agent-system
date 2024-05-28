package com.rsww.hotelService.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Repository
public class HotelRepositoryImpl implements HotelRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Hotel> findHotelsByLocation(final String location)
    {
        return entityManager.createQuery("SELECT h FROM Hotel h WHERE LOWER(h.city) = LOWER(:location)", Hotel.class)
            .setParameter("location", location)
            .getResultList();
    }

    @Override
    public <S extends Hotel> S save(final S entity)
    {
        final Hotel hotel = Hotel
            .builder()
            .withCity(entity.getCity())
            .withName(entity.getName())
            .withDescription(entity.getDescription())
            .build();
        entityManager.persist(hotel);
        return (S) hotel;
    }

    @Override
    public <S extends Hotel> Iterable<S> saveAll(final Iterable<S> entities)
    {
        entities.forEach(entity -> entityManager.persist(
            Hotel.builder()
                .withCity(entity.getCity())
                .withAreChildrenAllowed(entity.isAreChildrenAllowed())
                .withCountry(entity.getCountry())
                .withName(entity.getName())
                .withDescription(entity.getDescription())
                .withStarsNum(entity.getStarsNum())
                .build()
        ));
        return null;
    }

    @Override
    public Optional<Hotel> findById(final Integer integer)
    {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Integer integer)
    {
        return false;
    }

    @Override
    public List<Hotel> findAll()
    {
//        List<hotel> hotels = new ArrayList<>();
//        Connection connection = DatabaseConnection.getConnection();
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM hotel");
//            while (resultSet.next()) {
//                hotel hotel = new hotel();
//                // Assuming your hotel class has id, name, and location fields
//                hotel.setId(resultSet.getInt("id"));
//                hotel.setName(resultSet.getString("name"));
//                hotel.setLocation(resultSet.getString("location"));
//                hotels.add(hotel);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return hotels;
        return entityManager.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();
    }

    @Override
    public List<Hotel> findAllById(final Iterable<Integer> integers)
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
    public void delete(final Hotel entity)
    {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Integer> integers)
    {

    }

    @Override
    public void deleteAll(final Iterable<? extends Hotel> entities)
    {

    }

    @Override
    public void deleteAll()
    {

    }
}
