package org.tooldelta;

import lombok.SneakyThrows;
import org.tooldelta.logging.ColorPrint;
import java.io.UnsupportedEncodingException;
import org.tooldelta.logging.LogProcessor;

import org.tooldelta.server.WebSocketServerCore;
import org.tooldelta.client.WebSocketClientCore;

public class Main {
    public static final String WorkDir = System.getProperty("user.dir");

    public static void main(String[] args) throws UnsupportedEncodingException {
        ColorPrint.printLoad("ToolDelta Cloud Panel By SuperScript XingChen");
        ColorPrint.printLoad("ToolDelta Wiki: https://td-wiki.whiteleaf.cn/");
        ColorPrint.printLoad("ToolDelta 项目地址：https://github.com/ToolDelta");
        ColorPrint.printLoad(String.format("ToolDelta Cloud Panel v%s", Version.VERSION));
        ColorPrint.printSuc("ToolDelta Cloud Panel 已启动");
        BasicOperation();
    }

    @SneakyThrows
    public static Boolean BasicOperation() throws UnsupportedEncodingException {
        new Thread(LogProcessor::processLogs).start();
//        ServerSideControlMenu menu = new ServerSideControlMenu();
        WebSocketServerCore WSS =  new WebSocketServerCore();
        Thread.sleep(4000);
        WebSocketClientCore WSC = new WebSocketClientCore();
        return true;
    }

}
