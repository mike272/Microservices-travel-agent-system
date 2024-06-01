package com.rsww.apigateway.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


@Component
public class WebSocketEvents
{

    private final SimpMessagingTemplate template;

    public WebSocketEvents(final SimpMessagingTemplate template)
    {
        this.template = template;
    }

    @EventListener
    public void handleSessionConnected(final SessionConnectEvent event)
    {
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent("New WebSocket connection established")
            .build();
        template.convertAndSend("/app/send", message);
    }

    @EventListener
    public void handleSessionError(final SessionSubscribeEvent event)
    {
        final Message message = Message
            .builder()
            .withType("INFO")
            .withTextContent("error subscribing to the WebSocket")
            .build();
        template.convertAndSend("/app/send", message);
    }
}