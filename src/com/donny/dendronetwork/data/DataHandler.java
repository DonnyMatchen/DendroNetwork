package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.entry.*;
import com.donny.dendronetwork.json.JsonFormattingException;
import com.donny.dendronetwork.ltree.TraversalTree;
import com.donny.dendronetwork.ltree.TreeNode;

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

    //TODO TraceRt and Cache handling
    public void flushCaches() {
        readNodes().forEach(NodeEntry::flushCache);
    }

    public void buildCache(NodeEntry entry) {
        entry.flushCache();
        TraversalTree<NodeEntry> tree = new TraversalTree<>(entry);
        addToTree(tree, tree.HEAD, entry);
        try {
            for (NodeEntry node : readNodes()) {
                NodeEntry direction = tree.HEAD.getDirection(node);
            }
        } catch (LoopBackException ex) {
            //this just ensures that the entry doesn't end up in its own routing table
        }
    }

    public void addToTree(TraversalTree<NodeEntry> tree, TreeNode<NodeEntry> op, NodeEntry entry) {
        entry.getConnections().forEach(connection -> {
            TreeNode<NodeEntry> current = tree.add(connection.getOpposite(entry), connection.getDistance(), op);
            if (current != null) {
                addToTree(tree, current, connection.getOpposite(entry));
            }
        });
    }

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
