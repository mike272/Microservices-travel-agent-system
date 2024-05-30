package com.rsww.transport_service.transport;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
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
    //City,Country,Departure City,Departure Date,Return Date,Transport,Total Transport Places,Transport Prices aDULT,Transport Prices Baby,Transport Prices Kid,Transport Prices Teenanger
    private String departureCity;
    private String destinationCity;
    private String targetCity;
    private String targetCountry;
    @CsvBindByName(column = "departureDate")
    @CsvDate("yyyy-MM-dd")
    private Date departureDate;
    @CsvBindByName(column = "departureDate")
    @CsvDate("yyyy-MM-dd")
    private TransportTypeEnum transportType;
    private int basePrice;
    private int totalPlaces;
    private int availablePlaces;

}
