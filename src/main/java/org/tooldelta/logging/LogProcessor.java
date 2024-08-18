package org.tooldelta.logging;

import java.util.concurrent.BlockingQueue;

import org.tooldelta.server.WebSocketServerCore;
import java.util.Arrays;
import java.util.List;
import java.util.*;

public class LogProcessor {
    public static void processLogs() {
        BlockingQueue<String> logQueue = CustomOutputStream.getLogQueue();
        while (true) {
            try {
                String logMessage = logQueue.take();
                processLogMessage(logMessage);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static void processLogMessage(String logMessage) {
        List<String> logLevels = Arrays.asList("INFO", "DEBUG", "WARN", "ERROR", "TRACE", "FATAL");
        String logLevel = getLogLevel(logMessage, logLevels);
        if (logLevel != null) {
            LogHandler handler = LogHandlerFactory.getHandler(logLevel);
            if (handler != null) {
                String messageWithoutLevel = logMessage.substring(logLevel.length()).trim();
                handler.handle(messageWithoutLevel);
                MessageStatus(logMessage);
            }
        }
    }

    public static void MessageStatus(String logMessage) {
        String pattern = "Started Main in \\d+(\\.\\d+)? seconds \\(process running for \\d+(\\.\\d+)?\\)";
        java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(logMessage);
//        if (matcher.find()) {
//            ColorPrint.printSuc("ToolDelta Cloud Spring服务启动成功!");
//            WebSocketServerCore.ServerStatus = true;
//        }
    }

    public static String getLogLevel(String logMessage, List<String> logLevels) {
        for (String level : logLevels) {
            if (logMessage.startsWith(level)) {
                return level;
            }
        }
        return null;
    }

    interface LogHandler {
        void handle(String logMessage);
    }

    static class LogHandlerFactory {
        private static final Map<String, LogHandler> handlers = new HashMap<>();

        static {
            handlers.put("INFO", ColorPrint::printInf);
            handlers.put("WARN", ColorPrint::printWar);
            handlers.put("ERROR", ColorPrint::printErr);
            handlers.put("TRACE", ColorPrint::printErr);
            handlers.put("FATAL", ColorPrint::printErr);
        }

        public static LogHandler getHandler(String logLevel) {
            return handlers.get(logLevel);
        }
    }
}
