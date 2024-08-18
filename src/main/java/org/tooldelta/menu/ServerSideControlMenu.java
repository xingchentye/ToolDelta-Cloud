package org.tooldelta.menu;

import java.util.Scanner;

import org.tooldelta.logging.ColorPrint;

public class ServerSideControlMenu {
    private volatile boolean Running = true;

    public ServerSideControlMenu() {
        Thread InputThread = new Thread(() -> {
            Scanner CtrlScanner = new Scanner(System.in);
            while (Running) {
                if (CtrlScanner.hasNextLine()) {
                    String message = CtrlScanner.nextLine();
                    System.out.println(message);
                    if (message.equals("Menu") || message.equals("menu") || message.equals("菜单") || message.equals("?")) {
                        ControlToolMenu();
                    } else if (message.equals("exit")) {
                        Running = false;
                        System.exit(0);
                    }
                }
            }
            CtrlScanner.close();
        });
        InputThread.start();
    }

    public static void ControlToolMenu () {
        ColorPrint.printLoad("---- ToolDelta-Server Tool Menu ----");
        ColorPrint.printInf("(1) 添加可通行的身份认证账户");
//        ColorPrint.fmtInfo("请输入")
//        switch ()
    }

    public void Stop() {
        Running = false;
    }
}
