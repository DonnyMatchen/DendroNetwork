package com.donny.dendronetwork;

import com.donny.dendronetwork.data.*;

import java.io.File;

public class DendroNetwork {
    public static final LogHandler LOG_HANDLER = new LogHandler();
    public static final FileHandler FILE_HANDLER = new FileHandler();
    public static final UuidHandler UUID_HANDLER = new UuidHandler();
    public static final ImportHandler IMPORT_HANDLER = new ImportHandler();
    public static final ExportHandler EXPORT_HANDLER = new ExportHandler();
    public static final DataHandler DATA_HANDLER = new DataHandler();

    public static File data;
    public static LogHandler.LogLevel logLevel;

    public static void main(String[] args) {
        //TODO initialization code
    }
}
