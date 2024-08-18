package org.tooldelta.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class WebSocketServerApplication {
    public static void RunServer() {
        SpringApplication.run(WebSocketServerApplication.class);
    }
}

