package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.lnetwork.LConnection;
import com.donny.dendronetwork.data.lnetwork.LNode;
import com.donny.dendronetwork.instance.ProgramInstance;
import com.donny.dendroroot.fileio.FileHandler;
import com.donny.dendroroot.json.JsonArray;
import com.donny.dendroroot.json.JsonFormattingException;
import com.donny.dendroroot.json.JsonObject;

import java.io.File;

public class Saver extends FileHandler {
    private final ProgramInstance CURRENT_INSTANCE;
    public Saver(ProgramInstance instance) {
        super(instance);
        CURRENT_INSTANCE = instance;
    }

    public void save() {
        File nodesFile = new File(CURRENT_INSTANCE.data.getPath() + File.separator + "graph" + File.separator + "nodes.json");
        File consFile = new File(CURRENT_INSTANCE.data.getPath() + File.separator + "graph" + File.separator + "con.json");
        JsonArray nodes = new JsonArray();
        for(LNode node : CURRENT_INSTANCE.ALL_NODES.values()) {
            try {
                nodes.add(node.export());
            } catch (JsonFormattingException e) {

            }
        }
        JsonArray connections = new JsonArray();
        for(LConnection con : CURRENT_INSTANCE.ALL_CONNECTIONS) {
            try{
                connections.add(con.export());
            } catch (JsonFormattingException e) {

            }
        }
        writeJson(nodesFile, nodes);
        writeJson(consFile, connections);
    }
}
