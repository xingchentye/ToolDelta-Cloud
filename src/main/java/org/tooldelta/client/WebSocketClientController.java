package org.tooldelta.client;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketClientController {
    @Autowired
    WebSocketClient WsClient;
}

