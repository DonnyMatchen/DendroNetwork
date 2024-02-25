package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.instance.ProgramInstance;

public class DataHandler {
    public final Loader LOADER;
    public final Saver SAVER;

    public DataHandler(ProgramInstance instance) {
        LOADER = new Loader(instance);
        SAVER = new Saver(instance);
        instance.LOG_HANDLER.trace(getClass(), "DataHandler Initiated");
    }
}
