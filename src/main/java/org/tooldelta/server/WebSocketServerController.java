package org.tooldelta.server;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketServerController {
    @Autowired
    WebSocketServer WsServer;

}
