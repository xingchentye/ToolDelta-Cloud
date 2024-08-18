package org.tooldelta.server;

import org.tooldelta.logging.ColorPrint;
import org.tooldelta.Version;
import org.tooldelta.Utils;

public class WebSocketServerCore{
    public static final String CoreVersion = Version.VERSION;
    public static final String ServerName = "Main-Node";
    public static final String NodeUID = Utils.MakeNodeUID(8);
    public static Boolean ServerStatus = false;

    public WebSocketServerCore () {
        ColorPrint.printLoad(String.format("ToolDelta Server Core v%s 节点名称:%s 节点UID:%s", CoreVersion, ServerName, NodeUID));
        System.setProperty("spring.config.name", "application-server");
        WebSocketServerApplication.RunServer();
    }
}
