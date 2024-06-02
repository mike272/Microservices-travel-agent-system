package com.rsww.hotelService.hotel;

import java.util.List;

import com.rsww.hotelService.room.Room;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Table(name = "hotel")
public class Hotel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String city;
    private String country;
    private int starsNum;
    private boolean areChildrenAllowed;
    @Column(length = 10000)
    private String description;
    private double minPrice;

    private transient String roomsTmp;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;
}