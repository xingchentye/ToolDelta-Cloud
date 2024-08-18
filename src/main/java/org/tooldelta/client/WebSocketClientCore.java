package org.tooldelta.client;

import org.tooldelta.logging.ColorPrint;
import org.tooldelta.Version;

public class WebSocketClientCore {
    public static final String CoreVersion = Version.VERSION;

    public WebSocketClientCore () {
        ColorPrint.printLoad(String.format("ToolDelta Client Core v%s", CoreVersion));
        System.setProperty("spring.config.name", "application-client");
        WebSocketClientApplication.RunServer();
    }
}
