package com.rsww.transport_service.transport;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rsww.events.TransportsInitializedEvent;


@Service
public class TransportService
{

    private final TransportRepository transportRepository;
    private final EventGateway eventGateway;

    @Autowired
    public TransportService(final TransportRepository transportRepository,
                            final EventGateway eventGateway)
    {
        this.transportRepository = transportRepository;
        this.eventGateway = eventGateway;
    }

    public List<Transport> findAvailableTransports(final String departureLocation,
                                                   final String arrivalLocation,
                                                   final Date departureDate,
                                                   final Date returnDate,
                                                   final int adults,
                                                   final int children,
                                                   final int infants)
    {
        // not sure if I want to filter by number of people here.
        // I think it will be more informative to return all results to client and client will show that there are no available seats
        final var outgoingTransports = transportRepository.findDepartureFlights(departureLocation, arrivalLocation, departureDate);
        final var returnTransports = transportRepository.findReturnFlights(arrivalLocation, departureLocation, returnDate);
        return Stream.of(outgoingTransports, returnTransports).flatMap(List::stream).toList();
    }

    public com.rsww.dto.Transport mapToDtoTransport(final Transport transport)
    {
        return com.rsww.dto.Transport.builder()
            .withTransportType(transport.getTransportType())
            .withDepartureDate(transport.getDepartureDate())
            .withTotalNumberOfSeats(transport.getTotalPlaces())
            .withPricePerSeat(transport.getBasePrice())
            .build();
    }

    public List<com.rsww.dto.Transport> initializeTransports() throws IOException
    {
        final Reader reader = Files.newBufferedReader(Paths.get("./scrapTransport.csv"));
        final CsvToBean<Transport> csvToBean = new CsvToBeanBuilder<Transport>(reader)
            .withType(Transport.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        final List<Transport> transports = csvToBean.parse();
        final List<Transport> transportsSubList = transports.subList(0, Math.min(10, transports.size()));

        transportRepository.saveAll(transportsSubList);
        final var transportsList = transportsSubList.stream().map(this::mapToDtoTransport).toList();
        eventGateway.publish(TransportsInitializedEvent.builder().withTransports(transportsList).build());
        return transportsList;
    }
}
