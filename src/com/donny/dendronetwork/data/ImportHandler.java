package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;

import javax.swing.*;
import java.io.File;

public class ImportHandler {

    public enum ImportMode {
        /*
         * IGNORE: if there is a UUID clash, don't add the new one
         * KEEP: if there is a UUID clash, add the new one with a new UUID
         * OVERWRITE: if there is a UUID clash, discard the old one and add the new one
         */
        IGNORE, KEEP, OVERWRITE;

        public static String[] getArray() {
            return new String[]{
                    "IGNORE", "KEEP", "OVERWRITE"
            };
        }

        public static ImportMode fromString(String string) {
            return switch (string.toUpperCase()) {
                case "IGNORE", "IGNORE NEW" -> IGNORE;
                case "OVERWRITE", "REPLACE OLD" -> OVERWRITE;
                default -> KEEP;
            };
        }

        public String toString() {
            return switch (this) {
                case IGNORE -> "IGNORE";
                case KEEP -> "KEEP";
                case OVERWRITE -> "OVERWRITE";
            };
        }
    }

    public ImportHandler() {
        DendroNetwork.LOG_HANDLER.trace(getClass(), "ImportHandler initiated");
    }

    /*
     * It is expected that JSON imports might be encrypted, but CSVs never will be
     */
    public final void load(String path, JFrame caller, ImportMode mode) {
        File file = new File(path);
        if (file.exists()) {
            if (path.toLowerCase().contains(".csv")) {
                loadCSV(file);
            } else if (path.toLowerCase().contains(".json")) {
                loadJSON(file, mode);
            }
        }
    }

    public void loadCSV(File file) {
        //TODO import CSV
    }

    public void loadJSON(File file, ImportMode mode) {
        //TODO import JSON
    }
}
