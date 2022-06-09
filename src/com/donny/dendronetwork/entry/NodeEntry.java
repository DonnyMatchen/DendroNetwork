package com.donny.dendronetwork.entry;

import com.donny.dendronetwork.data.ImportHandler;
import com.donny.dendronetwork.json.*;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeEntry extends Entry {
    private final String NAME;
    private final HashMap<Long, Long> ROUTING_TABLE;
    private final ArrayList<ConnectionEntry> CONNECTIONS;

    public NodeEntry(String name) {
        super();
        NAME = name;
        ROUTING_TABLE = new HashMap<>();
        CONNECTIONS = new ArrayList<>();
    }

    public NodeEntry(JsonObject object, ImportHandler.ImportMode mode) {
        super(object, mode);
        NAME = object.getString("name").getString();
        ROUTING_TABLE = new HashMap<>();
        object.getArray("rt-tbl").getObjectArray().forEach(obj -> ROUTING_TABLE.put(
                obj.getDecimal("dst").decimal.longValue(),
                obj.getDecimal("rt").decimal.longValue()
        ));
        CONNECTIONS = new ArrayList<>();
    }

    public String getName() {
        return NAME;
    }

    public void flushCache() {
        ROUTING_TABLE.clear();
    }

    public ConnectionEntry route(NodeEntry dst) throws LoopBackException {
        if (dst.equals(this)) {
            throw new LoopBackException();
        } else {
            Long uuid = ROUTING_TABLE.get(dst.getUUID());
            if (uuid != null) {
                for (ConnectionEntry entry : getConnections()) {
                    if (entry.getOpposite(this).getUUID() == uuid) {
                        return entry;
                    }
                }
            }
            return null;
        }
    }

    public ArrayList<ConnectionEntry> getConnections() {
        return new ArrayList<>(CONNECTIONS);
    }

    public boolean addConnection(ConnectionEntry entry) {
        return CONNECTIONS.add(entry);
    }

    @Override
    public JsonObject export() throws JsonFormattingException {
        JsonObject object = super.export();
        object.put("name", new JsonString(NAME));
        JsonArray array = new JsonArray();
        for (long dst : ROUTING_TABLE.keySet()) {
            JsonObject rt = new JsonObject();
            rt.put("dst", new JsonDecimal(dst));
            rt.put("rt", new JsonDecimal(ROUTING_TABLE.get(dst)));
            array.add(rt);
        }
        object.put("rt-tbl", array);
        return object;
    }

    @Override
    public String toFlatString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getUUID()).append("\t{")
                .append(NAME);
        ROUTING_TABLE.forEach((dst, dir) -> builder.append(dst).append(":").append(dir).append(", "));
        builder.append("}");
        return builder.toString().replace(", }", "}");
    }

    @Override
    public String toString() {
        return NAME;
    }
}
