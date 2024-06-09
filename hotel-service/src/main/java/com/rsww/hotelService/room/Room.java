package com.rsww.hotelService.room;

import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.rsww.hotelService.hotel.Hotel;
import com.rsww.hotelService.reservation.Reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numberOfPeople;
    private double basePrice;

    @ManyToOne(fetch = FetchType.LAZY)
//    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
    // fields parsed from csv
    private String roomName;
    private int capacityAdults;
    private int capacityKids;
    private double price;
    private int availableRooms;
}
