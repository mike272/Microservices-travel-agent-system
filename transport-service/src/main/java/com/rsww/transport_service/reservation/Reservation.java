package com.rsww.transport_service.reservation;

import com.rsww.transport_service.transport.Transport;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;


@Entity
@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class Reservation {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Transport transport;

    private int reservedPlaces;
    private int clientId;
    private int adultPlaces;
    private int childPlaces;
    private int infantPlaces;
}