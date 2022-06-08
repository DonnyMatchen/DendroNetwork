package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.entry.ConnectionEntry;
import com.donny.dendronetwork.entry.Entry;
import com.donny.dendronetwork.entry.EntryType;
import com.donny.dendronetwork.entry.NodeEntry;
import com.donny.dendronetwork.json.JsonFormattingException;

import java.util.ArrayList;

public class DataHandler {
    private final DataSet<NodeEntry> NODES;
    private final DataSet<ConnectionEntry> CONNECTIONS;

    public DataHandler() {
        NODES = new DataSet<>("Nodes", EntryType.NODE);
        CONNECTIONS = new DataSet<>("Connections", EntryType.CONNECTION);
        DendroNetwork.LOG_HANDLER.trace(getClass(), "DataHandler Initiated");
    }

    public void reload() {
        DendroNetwork.UUID_HANDLER.UUIDS.clear();
        try {
            NODES.reload();
            CONNECTIONS.reload();
        } catch (JsonFormattingException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), "Error loading datasets: " + e);
        }
    }

    public NodeEntry getNode(long uuid) {
        for (NodeEntry node : readNodes()) {
            if (node.getUUID() == uuid) {
                return node;
            }
        }
        return null;
    }

    public ConnectionEntry getConnection(long uuid) {
        for (ConnectionEntry node : readConnections()) {
            if (node.getUUID() == uuid) {
                return node;
            }
        }
        return null;
    }

    public Entry getEntry(long uuid) {
        NodeEntry nodeEntry = getNode(uuid);
        if (nodeEntry != null) {
            return nodeEntry;
        }
        ConnectionEntry connectionEntry = getConnection(uuid);
        if (connectionEntry != null) {
            return connectionEntry;
        }

        return null;
    }

    //TODO data sets adders

    //TODO data sets deleters

    public ArrayList<NodeEntry> readNodes() {
        try {
            return NODES.read();
        } catch (JsonFormattingException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), "Error loading datasets: " + e);
            return new ArrayList<>();
        }
    }

    public ArrayList<ConnectionEntry> readConnections() {
        try {
            return CONNECTIONS.read();
        } catch (JsonFormattingException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), "Error loading datasets: " + e);
            return new ArrayList<>();
        }
    }

    //TODO data sets hunters

    public void save() {
        NODES.save();
        CONNECTIONS.save();
    }

    //TODO data checks
}
