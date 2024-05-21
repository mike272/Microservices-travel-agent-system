package com.rsww.hotelService.room;

import java.util.List;

import com.rsww.hotelService.hotel.Hotel;
import com.rsww.hotelService.reservation.Reservation;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfPeople;
    private double basePrice;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}
