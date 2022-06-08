package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;

import java.io.File;

public class ExportHandler {
    private final File DIR;

    public ExportHandler() {
        DIR = new File(DendroNetwork.data.getPath() + File.separator + "Exports");
        DendroNetwork.LOG_HANDLER.trace(getClass(), "ExportHandler initiated");
    }

    public void export(String extension, String name) {
        //TODO export method
    }
}
