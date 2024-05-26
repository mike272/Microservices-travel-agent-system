

@Service
public class TransportCommandHandler{

    private final TransportService transportService;
    private final EventGateway eventGateway;
    private final CommandGateway commandGateway;

    public TransportCommandHandler(TransportService transportService, EventGateway eventGateway, CommandGateway commandGateway) {
        this.transportService = transportService;
        this.eventGateway = eventGateway;
        this.commandGateway = commandGateway;
    }

}

