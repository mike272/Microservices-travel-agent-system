package com.rsww.transport_service.reservation;

import com.rsww.transport_service.transport.Transport;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
@Table(name = "transportReservation")
public class Reservation {
    @Id
    private int reservation_id;

    @ManyToOne
    @JoinColumn(name = "id")
    private Transport transport;

    private int reservedPlaces;
    private int clientId;
    private int adultPlaces;
    private int childPlaces;
    private int infantPlaces;
}