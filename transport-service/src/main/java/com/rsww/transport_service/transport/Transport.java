package com.rsww.transport_service.transport;

import java.util.Date;

import com.rsww.dto.TransportTypeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Table(name = "transport")
public class Transport
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String departureCity;
    private String destinationCity;
    private Date departureDate;
    private TransportTypeEnum transportType;
    private int basePrice;
    private int totalPlaces;
    private int availablePlaces;

}
