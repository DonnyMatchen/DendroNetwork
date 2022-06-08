package com.donny.dendronetwork;

import com.donny.dendronetwork.data.*;

import java.io.File;

public class DendroNetwork {
    public static final LogHandler LOG_HANDLER;
    public static final FileHandler FILE_HANDLER;
    public static final UuidHandler UUID_HANDLER;
    public static final ImportHandler IMPORT_HANDLER;
    public static final ExportHandler EXPORT_HANDLER;
    public static final DataHandler DATA_HANDLER;

    public static File data;
    public static LogHandler.LogLevel logLevel;

    static{
        LOG_HANDLER = new LogHandler();
        FILE_HANDLER = new FileHandler();
        UUID_HANDLER = new UuidHandler();
        IMPORT_HANDLER = new ImportHandler();
        EXPORT_HANDLER = new ExportHandler();
        DATA_HANDLER = new DataHandler();
    }

    public static void main(String[] args) {
        data = new File(System.getProperty("user.dir") + File.separator + "data");
        logLevel = new LogHandler.LogLevel("warn");
        //TODO handle program arguments
        //TODO initialization code
    }
}
