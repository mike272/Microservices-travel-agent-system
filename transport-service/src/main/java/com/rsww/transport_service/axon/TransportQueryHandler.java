import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;


@Service
public class TransportQueryHandler {
    private final TransportService transportService;
    private final EventGateway eventGateway;
    private final CommandGateway commandGateway;


    public TransportQueryHandler(TransportService transportService, EventGateway eventGateway, CommandGateway commandGateway) {
        this.transportService = transportService;
        this.eventGateway = eventGateway;
        this.commandGateway = commandGateway;
    }
}