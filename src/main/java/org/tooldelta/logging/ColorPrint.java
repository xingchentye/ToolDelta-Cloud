package org.tooldelta.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class ColorPrint {
    private static final String INFO_NORMAL = "§f 信息 ";
    private static final String INFO_WARN = "§6 警告 ";
    private static final String INFO_ERROR = "§4 报错 ";
    private static final String INFO_FAIL = "§c 失败 ";
    private static final String INFO_SUCC = "§a 成功 ";
    private static final String INFO_LOAD = "§d 加载 ";
    private static final String[][] STD_COLOR_LIST = {
            {"0", "#000000"},
            {"1", "#0000AA"},
            {"2", "#00AA00"},
            {"3", "#00AAAA"},
            {"4", "#AA0000"},
            {"5", "#AA00AA"},
            {"6", "#FFAA00"},
            {"7", "#AAAAAA"},
            {"8", "#555555"},
            {"9", "#5555FF"},
            {"a", "#55FF55"},
            {"b", "#55FFFF"},
            {"c", "#FF5555"},
            {"d", "#FF55FF"},
            {"e", "#FFFF55"},
            {"f", "#FFFFFF"},
            {"g", "#DDD605"},
            {"r", "/"},
    };

    public static final ReentrantLock Lock = new ReentrantLock();

    public static String simpleFmt(String[][] kw, String arg) {
        for (String[] kv : kw) {
            arg = arg.replace(kv[0], kv[1]);
        }
        return arg;
    }

    public static String colormodeReplace(String text, int showmode) {
        text = strike(text);
        return simpleFmt(new String[][]{
                {"§0", "\033[" + showmode + ";37;90m"},
                {"§1", "\033[" + showmode + ";37;34m"},
                {"§2", "\033[" + showmode + ";37;32m"},
                {"§3", "\033[" + showmode + ";37;36m"},
                {"§4", "\033[" + showmode + ";37;31m"},
                {"§5", "\033[" + showmode + ";37;35m"},
                {"§6", "\033[" + showmode + ";37;33m"},
                {"§7", "\033[" + showmode + ";37;90m"},
                {"§8", "\033[" + showmode + ";37;2m"},
                {"§9", "\033[" + showmode + ";37;94m"},
                {"§a", "\033[" + showmode + ";37;92m"},
                {"§b", "\033[" + showmode + ";37;96m"},
                {"§c", "\033[" + showmode + ";37;91m"},
                {"§d", "\033[" + showmode + ";37;95m"},
                {"§e", "\033[" + showmode + ";37;93m"},
                {"§f", "\033[" + showmode + ";37;1m"},
                {"§r", "\033[0m"},
                {"§u", "\033[4m"},
                {"§l", "\033[1m"},
        }, text) + "\033[0m";
    }

    public static String align(String text, int length) {
        int textLength = text.length();
        for (char c : text.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                textLength += 1;
            }
        }
        return text + " ".repeat(Math.max(0, length - textLength));
    }

    private static String strike(String text) {
        StringBuilder textOk = new StringBuilder();
        boolean strikeMode = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '§') {
                if (i + 1 < text.length()) {
                    if (text.charAt(i + 1) == 's') {
                        strikeMode = true;
                        i++;
                        continue;
                    }
                    if (text.charAt(i + 1) == 'r') {
                        strikeMode = false;
                        i++;
                        continue;
                    }
                }
            }
            if (strikeMode) {
                textOk.append("\u0336").append(c);
            } else {
                textOk.append(c);
            }
        }
        return textOk.toString();
    }

    public static void printWithInfo(String text, String info, boolean needLog) {
        Lock.lock();
        try {
            if (needLog) {
                cLog(info, text);
            }
            String setNextColor = "§r";
            if (text.contains("\n")) {
                String[] lines = text.split("\n");
                StringBuilder outputTxts = new StringBuilder();
                for (String line : lines) {
                    if (line.contains("§")) {
                        int n = line.lastIndexOf("§");
                        if (n != -1 && n + 1 < line.length()) {
                            setNextColor = line.substring(n, n + 2);
                        }
                    }
                    outputTxts.append(new SimpleDateFormat("[HH:mm] ").format(new Date()))
                            .append(colormodeReplace(info, 7))
                            .append(" ")
                            .append(colormodeReplace(setNextColor + line, 0))
                            .append("\n");
                }
                System.out.print(outputTxts.toString());
            } else {
                System.out.println(new SimpleDateFormat("[HH:mm] ").format(new Date())
                        + colormodeReplace(info, 7)
                        + " "
                        + colormodeReplace(text, 0));
            }
        } finally {
            Lock.unlock();
        }
    }

    public static void cleanPrint(String text) {
        Lock.lock();
        try {
            System.out.println(colormodeReplace(text, 0));
        } finally {
            Lock.unlock();
        }
    }

    public static String cleanFmt(String text) {
        return colormodeReplace(text, 0);
    }

    public static void print(String... args) {
        printWithInfo(String.join(" ", args), INFO_NORMAL, true);
    }

    public static void printErr(String text) {
        printWithInfo("§c" + text, INFO_ERROR, true);
    }

    public static void printInf(String text) {
        printWithInfo(text, INFO_NORMAL, true);
    }

    public static void printSuc(String text) {
        printWithInfo("§a" + text, INFO_SUCC, true);
    }

    public static void printWar(String text) {
        printWithInfo("§6" + text, INFO_WARN, true);
    }

    public static void printLoad(String text) {
        Lock.lock();
        try {
            printWithInfo("§d" + text, INFO_LOAD, true);
        } finally {
            Lock.unlock();
        }
    }

    public static String fmtInfo(String text, String info) {
        Lock.lock();
        try {
            String nextcolor = "§r";
            if (text.contains("\n")) {
                String[] lines = text.split("\n");
                StringBuilder outputTxts = new StringBuilder();
                for (String line : lines) {
                    if (line.contains("§")) {
                        int n = line.lastIndexOf("§");
                        if (n != -1 && n + 1 < line.length()) {
                            nextcolor = line.substring(n, n + 2);
                        }
                    }
                    outputTxts.append(new SimpleDateFormat("[HH:mm] ").format(new Date()))
                            .append(colormodeReplace(info, 7))
                            .append(" ")
                            .append(colormodeReplace(nextcolor + line, 0))
                            .append("\n");
                }
                return outputTxts.toString();
            }
            return new SimpleDateFormat("[HH:mm] ").format(new Date())
                    + colormodeReplace(info, 7)
                    + " "
                    + colormodeReplace(text, 0);
        } finally {
            Lock.unlock();
        }
    }

    public static void cLog(String inf, String msg) {
        Lock.lock();
        try {
            String[] logLevels = {"§6 警告 ", "WARN", "§a 成功 ", "INFO", "§f 信息 ", "INFO", "§c 失败 ", "FAIL", "§4 报错 ", "ERROR"};
            for (int i = 0; i < logLevels.length; i += 2) {
                if (inf.equals(logLevels[i])) {
                    inf = logLevels[i + 1];
                    break;
                }
            }
            for (String[] col : STD_COLOR_LIST) {
                String colorCode = "§" + col[0];
                msg = msg.replace(colorCode, "");
            }
            for (String[] col : STD_COLOR_LIST) {
                String colorCode = "§" + col[0];
                inf = inf.replace(colorCode, "");
            }
            inf = inf.replace(" ", "");
            // Assuming publicLogger.log_in is a method to log messages
            // publicLogger.log_in(msg, inf);
        } finally {
            Lock.unlock();
        }
    }

}
