package com.donny.dendronetwork.data;

import com.donny.dendronetwork.data.lnetwork.LNetwork;
import com.donny.dendronetwork.instance.ProgramInstance;
import com.donny.dendroroot.fileio.FileHandler;
import com.donny.dendroroot.json.JsonArray;
import com.donny.dendroroot.json.JsonObject;

import java.io.File;

public class Loader extends FileHandler {
    private final ProgramInstance CURRENT_INSTANCE;

    public Loader(ProgramInstance instance) {
        super(instance);
        CURRENT_INSTANCE = instance;
    }

    public void load() {
        File nodesFile = new File(CURRENT_INSTANCE.data.getPath() + File.separator + "graph" + File.separator + "nodes.json");
        File consFile = new File(CURRENT_INSTANCE.data.getPath() + File.separator + "graph" + File.separator + "con.json");
        if (nodesFile.exists()) {
            JsonArray nodes = (JsonArray) readJson(nodesFile);
            if (nodes.size() > 0) {
                for (JsonObject rawNode : nodes.getObjectArray()) {
                    String network = rawNode.getString("n").getString();
                    if (!CURRENT_INSTANCE.NETWORKS.containsKey(network)) {
                        new LNetwork(network, CURRENT_INSTANCE);
                    }
                    LNetwork net = CURRENT_INSTANCE.NETWORKS.get(network);
                    net.addNode(rawNode.getString("i").getString(), rawNode.getString("d").getString());
                    net.getNode(rawNode.getString("i").getString()).loadRoutes(rawNode.getObject("r"));
                }
            }
        }
        if (consFile.exists()) {
            JsonArray cons = (JsonArray) readJson(consFile);
            if (cons.size() > 0) {
                for (JsonObject rawCon : cons.getObjectArray()) {
                    LNetwork net = CURRENT_INSTANCE.NETWORKS.get(rawCon.getString("n").getString());
                    net.addConnection(
                            rawCon.getString("a").getString(),
                            rawCon.getString("b").getString(),
                            rawCon.getDecimal("c").decimal
                    );
                }
            }
        }
    }
}
