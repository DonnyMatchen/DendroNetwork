package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHandler {
    /*
     * regardless of log level, LOG stores all log statements bellow trace level
     * regardless of log level, TRACE stores all log statements
     */
    private final StringBuilder LOG, TRACE;

    public LogHandler() {
        LOG = new StringBuilder();
        TRACE = new StringBuilder();
        trace(getClass(), "Log Handler Initiated");
    }

    public void print(String str, boolean print) {
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("{MMM dd yyyy} (hh:mm:ss a z) ");
        TRACE.append(format.format(now)).append(str).append("\n");
        LOG.append(format.format(now)).append(str).append("\n");
        if (print) {
            System.out.println(format.format(now) + str);
        }
    }

    public void save() {
        File dir = new File(DendroNetwork.data.getPath() + File.separator + "Logs");
        if (!dir.exists()) {
            dir.mkdir();
        }
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH;mm;ss_z");
        DendroNetwork.FILE_HANDLER.writePlain(dir, format.format(now) + ".log", LOG.toString());
        DendroNetwork.FILE_HANDLER.writePlain(dir, format.format(now) + ".trace.log", TRACE.toString());
    }

    public void fatal(Class cause, String message) {
        print("[" + cause.toString().split(" ")[1].substring(24) + "/FATAL] " + message, DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.FATAL);
    }

    public void error(Class cause, String message) {
        print("[" + cause.toString().split(" ")[1].substring(24) + "/ERROR] " + message, DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.ERROR);
    }

    public void warn(Class cause, String message) {
        print("[" + cause.toString().split(" ")[1].substring(24) + "/WARN] " + message, DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.WARN);
    }

    public void info(Class cause, String message) {
        print("[" + cause.toString().split(" ")[1].substring(24) + "/INFO] " + message, DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.INFO);
    }

    public void debug(Class cause, String message) {
        print("[" + cause.toString().split(" ")[1].substring(24) + "/DEBUG] " + message, DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.DEBUG);
    }

    public final void trace(Class cause, String message) {
        String str = "[" + cause.toString().split(" ")[1].substring(24) + "/TRACE] " + message;
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("{MMM dd yyyy} (hh:mm:ss a z) ");
        TRACE.append(format.format(now)).append(str).append("\n");
        if (DendroNetwork.logLevel != null && DendroNetwork.logLevel.getLevel() >= LogLevel.TRACE) {
            System.out.println(format.format(now) + str);
        }
    }

    public static class LogLevel {
        public static final int OFF = 0;
        public static final int FATAL = 1;
        public static final int ERROR = 2;
        public static final int WARN = 3;
        public static final int INFO = 4;
        public static final int DEBUG = 5;
        public static final int TRACE = 6;

        private final String NAME;

        public LogLevel(String name) {
            NAME = name;
        }

        public int getLevel() {
            return switch (NAME) {
                case "off" -> OFF;
                case "fatal" -> FATAL;
                case "error" -> ERROR;
                case "warn" -> WARN;
                case "debug" -> DEBUG;
                case "trace", "all" -> TRACE;
                default -> INFO;
            };
        }

        public String getName() {
            return NAME;
        }
    }
}
