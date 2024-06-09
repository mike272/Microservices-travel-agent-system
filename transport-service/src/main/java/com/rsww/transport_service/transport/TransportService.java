package com.rsww.transport_service.transport;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.rsww.commands.ConfirmTransportReservationCommand;
import com.rsww.dto.ReservationEventType;
import com.rsww.events.TransportReservationEvent;
import com.rsww.events.TransportsInitializedEvent;
import com.rsww.responses.AvailableTransportsResponse;


@Service
public class TransportService
{

    private final TransportRepository transportRepository;
    private final EventGateway eventGateway;
    private final Logger logger = LoggerFactory.getLogger(TransportService.class);

    @Autowired
    public TransportService(final TransportRepository transportRepository,
                            final EventGateway eventGateway)
    {
        this.transportRepository = transportRepository;
        this.eventGateway = eventGateway;
    }

    public AvailableTransportsResponse findAvailableTransports(final String departureLocation,
                                                               final String arrivalLocation,
                                                               final Date departureDate,
                                                               final Date returnDate,
                                                               final int adults,
                                                               final int children,
                                                               final int infants)
    {
        // not sure if I want to filter by number of people here.
        // I think it will be more informative to return all results to client and client will show that there are no available seats
        final PageRequest pageRequest = PageRequest.of(0, 10);
        // for debugging purposes
        final var allTransports = transportRepository.findAll();
        final var outgoingTransports = transportRepository.findFlights(departureLocation, arrivalLocation, departureDate, pageRequest);
        final var returnTransports = transportRepository.findFlights(arrivalLocation, departureLocation, returnDate, pageRequest);
        return AvailableTransportsResponse
            .builder()
            .withTransports(new ArrayList<>(Stream.of(outgoingTransports, returnTransports)
                .flatMap(List::stream)
                .map(this::mapToDtoTransport)
                .toList()))
            .build();
    }

    public com.rsww.dto.Transport mapToDtoTransport(final Transport transport)
    {
        return com.rsww.dto.Transport.builder()
            .withTransportType(transport.getTransportType())
            .withDepartureDate(transport.getDepartureDate())
            .withTotalNumberOfSeats(transport.getTotalPlaces())
            .withBasePrice(transport.getBasePrice())
            .withAvailablePlaces(transport.getAvailablePlaces())
            .withDepartureCity(transport.getDepartureCity())
            .withDestinationCity(transport.getDestinationCity())
            .withDepartureCountry(transport.getDepartureCountry())
            .withDestinationCountry(transport.getDestinationCountry())
            .build();
    }

    public void initializeTransports() throws IOException
    {
        final Reader reader = Files.newBufferedReader(Paths.get("./src/main/java/com/rsww/transport_service/transport/scrapTransport.csv"));
        final CsvToBean<Transport> csvToBean = new CsvToBeanBuilder<Transport>(reader)
            .withType(Transport.class)
            .withFilter(line -> Arrays.stream(line).allMatch(s -> s != null && !s.isEmpty() && !s.equals("NULL")))
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        final long count = transportRepository.count();
        final List<Transport> transports = csvToBean.parse();
        final List<Transport> transportsSubList = transports
            .stream()
            .skip(count)
            .limit(500)
            .map(transport -> {
                transport.setAvailablePlaces(transport.getTotalPlaces());
                return transport;
            })
            .toList();

        final List<Transport> reverseTransports = transportsSubList.stream()
            .map(transport -> {
                final Transport reverseTransport = new Transport();
                reverseTransport.setDepartureCity(transport.getDestinationCity());
                reverseTransport.setDestinationCity(transport.getDepartureCity());
                reverseTransport.setDepartureCountry(transport.getDestinationCountry());
                reverseTransport.setDestinationCountry(transport.getDepartureCountry());
                reverseTransport.setDepartureDate(transport.getDepartureDate());
                reverseTransport.setBasePrice(transport.getBasePrice());
                reverseTransport.setAvailablePlaces(transport.getTotalPlaces());
                reverseTransport.setTotalPlaces(transport.getTotalPlaces());
                reverseTransport.setTransportType(transport.getTransportType());
                return reverseTransport;
            })
            .toList();
        transportRepository.saveAll(transportsSubList);
        transportRepository.saveAll(reverseTransports);

        final var transportsList = new ArrayList<>(transportsSubList
            .stream()
            .map(this::mapToDtoTransport)
            .toList());
        eventGateway.publish(TransportsInitializedEvent.builder().withTransports(transportsList).build());
    }

    public void reserveTransports(
        final int tripReservationId,
        final int outboundTransportId,
        final Date outboundDate,
        final int returnTransportId,
        final Date returnDate,
        final int numOfAdults,
        final int numOfChildren,
        final int numOfInfants)
    {
        final var outboundTransport = transportRepository.findById(outboundTransportId).orElseThrow(() -> {
            final TransportReservationEvent transportReservationEvent = TransportReservationEvent.builder()
                .withTripReservationId(tripReservationId)
                .withTransportEventId(outboundTransportId)
                .withStatus(ReservationEventType.FAILED)
                .build();
            eventGateway.publish(transportReservationEvent);

            logger.error("Transport not found for id: {}", outboundTransportId);
            return new RuntimeException("Transport not found for id: " + outboundTransportId);
        });
        final var returnTransport = transportRepository.findById(returnTransportId).orElseThrow(() -> {
            final TransportReservationEvent transportReservationEvent = TransportReservationEvent.builder()
                .withTripReservationId(tripReservationId)
                .withTransportEventId(returnTransportId)
                .withStatus(ReservationEventType.FAILED)
                .build();
            eventGateway.publish(transportReservationEvent);

            logger.error("Transport not found for id: {}", returnTransportId);
            return new RuntimeException("Transport not found for id: " + returnTransportId);
        });

        if (outboundTransport.getAvailablePlaces() < numOfAdults + numOfChildren + numOfInfants)
        {
            final TransportReservationEvent transportReservationEvent = TransportReservationEvent.builder()
                .withTripReservationId(tripReservationId)
                .withTransportEventId(outboundTransportId)
                .withStatus(ReservationEventType.FAILED)
                .build();
            eventGateway.publish(transportReservationEvent);
            throw new RuntimeException("Not enough places available for outbound transport");
        }
        if (returnTransport.getAvailablePlaces() < numOfAdults + numOfChildren + numOfInfants)
        {
            final TransportReservationEvent transportReservationEvent = TransportReservationEvent.builder()
                .withTripReservationId(tripReservationId)
                .withTransportEventId(returnTransportId)
                .withStatus(ReservationEventType.FAILED)
                .build();
            eventGateway.publish(transportReservationEvent);
            throw new RuntimeException("Not enough places available for return transport");
        }

        outboundTransport.setAvailablePlaces(outboundTransport.getAvailablePlaces() - numOfAdults - numOfChildren - numOfInfants);
        returnTransport.setAvailablePlaces(returnTransport.getAvailablePlaces() - numOfAdults - numOfChildren - numOfInfants);

        transportRepository.save(outboundTransport);
        transportRepository.save(returnTransport);

        final TransportReservationEvent transportReservationEvent = TransportReservationEvent.builder()
            .withTripReservationId(tripReservationId)
            .withTransportEventId(outboundTransportId)
            .withStatus(ReservationEventType.CREATED)
            .build();

        eventGateway.publish(transportReservationEvent);
    }

    public void confirmTransportReservation(final int tripReservationId)
    {
        logger.info("Confirming transport reservation for trip: {}", tripReservationId);
//        final var transports = transportRepository.findByTripReservationId(tripReservation
//    TODO:: implement transport reservation logic, right now it's just a mock
        final var transportConfirmedEvent = TransportReservationEvent.builder()
            .withTripReservationId(tripReservationId)
            .withStatus(ReservationEventType.CONFIRMED)
            .build();

        eventGateway.publish(transportConfirmedEvent);
    }

}
