package com.donny.dendronetwork.instance;

import com.donny.dendronetwork.data.DataHandler;
import com.donny.dendronetwork.data.lnetwork.LConnection;
import com.donny.dendronetwork.data.lnetwork.LNetwork;
import com.donny.dendronetwork.data.lnetwork.LNode;
import com.donny.dendroroot.instance.Instance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramInstance extends Instance {
    public final HashMap<String, LNetwork> NETWORKS;
    public final HashMap<String, LNode> ALL_NODES;
    public final ArrayList<LConnection> ALL_CONNECTIONS;
    public final DataHandler DATA_HANDLER;

    public File data;

    public ProgramInstance(String iid, String[] args) {
        super(iid, args);
        NETWORKS = new HashMap<>();
        ALL_NODES = new HashMap<>();
        ALL_CONNECTIONS = new ArrayList<>();

        DATA_HANDLER = new DataHandler(this);
        data = new File(System.getProperty("user.dir") + File.separator + "data");
        DATA_HANDLER.LOADER.load();
    }

    @Override
    public void save() {
        DATA_HANDLER.SAVER.save();
    }

}
