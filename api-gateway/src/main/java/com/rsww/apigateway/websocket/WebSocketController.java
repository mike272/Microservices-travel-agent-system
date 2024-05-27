package com.rsww.apigateway.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send")
    @SendTo("/subscribe")
    public Message handleEvent(final Message message) {
        return message;
    }
}
