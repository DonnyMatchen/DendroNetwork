package com.donny.dendronetwork.entry;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.ImportHandler;
import com.donny.dendronetwork.json.JsonArray;
import com.donny.dendronetwork.json.JsonDecimal;
import com.donny.dendronetwork.json.JsonFormattingException;
import com.donny.dendronetwork.json.JsonObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

public class ConnectionEntry extends Entry {
    private final HashSet<NodeEntry> NODES;
    private final BigDecimal DISTANCE;

    public ConnectionEntry(NodeEntry[] nodes, BigDecimal distance) {
        super();
        NODES = new HashSet<>();
        if (nodes.length == 2) {
            NODES.addAll(Arrays.asList(nodes));
        } else {
            throw new IllegalArgumentException("Exactly two nodes are required for a ConnectionEntry");
        }
        NODES.forEach(node -> node.addConnection(this));
        DISTANCE = distance;
    }

    public ConnectionEntry(JsonObject object, ImportHandler.ImportMode mode) {
        super(object, mode);
        NODES = new HashSet<>();
        JsonArray arr = object.getArray("nodes");
        if (arr.size() == 2) {
            arr.getDecimalArray().forEach(dec -> NODES.add(DendroNetwork.DATA_HANDLER.getNode(dec.decimal.longValue())));
        } else {
            throw new IllegalArgumentException("Exactly two nodes are required for a ConnectionEntry");
        }
        DISTANCE = object.getDecimal("dist").decimal;
    }

    public String getName() {
        return NODES.toString();
    }

    public HashSet<NodeEntry> getNodes() {
        return new HashSet<>(NODES);
    }

    public BigDecimal getDistance() {
        return DISTANCE;
    }

    @Override
    public JsonObject export() throws JsonFormattingException {
        JsonObject object = super.export();
        JsonArray array = new JsonArray();
        NODES.forEach(node -> array.add(new JsonDecimal(node.getUUID())));
        object.put("nodes", array);
        object.put("dist", new JsonDecimal(DISTANCE));
        return object;
    }

    @Override
    public String toFlatString() {
        return getName() + "\t" + DISTANCE;
    }

    @Override
    public String toString() {
        return getName();
    }
}
