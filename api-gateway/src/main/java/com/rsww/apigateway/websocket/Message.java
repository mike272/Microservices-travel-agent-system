package com.rsww.apigateway.websocket;

import lombok.Builder;
import lombok.Data;


@Data
@Builder(setterPrefix = "with")
public class Message
{
    private String textContent;
    private String type;
}
