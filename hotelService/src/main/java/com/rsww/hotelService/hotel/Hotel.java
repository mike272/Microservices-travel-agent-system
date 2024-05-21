package com.rsww.hotelService.hotel;

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
    private String description;

//    @OneToMany(mappedBy = "hotel")
//    private Set<Hotelreservation> hotelreservations = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "hotel")
//    private Set<Hotelroom> hotelrooms = new LinkedHashSet<>();

//    private String location;
//    private Double minPrice;

//    @OneToMany(mappedBy = "hotel")
//    private List<Room> rooms;
}