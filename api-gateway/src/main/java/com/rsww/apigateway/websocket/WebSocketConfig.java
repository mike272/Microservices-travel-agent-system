package com.rsww.apigateway.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config)
    {
        // topic for broadcasting messages
        config.enableSimpleBroker("/topic");
//        this is for the client to send messages to the server. We're not gonna use it
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry)
    {
        // endpoint for the client to connect to the server
        registry.addEndpoint("/subscribe").setAllowedOriginPatterns("*").withSockJS();
    }
}