package com.donny.dendronetwork.entry;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.ImportHandler;
import com.donny.dendronetwork.json.*;
import com.donny.dendronetwork.util.ExportableToJson;

import java.util.ArrayList;

public class NodeEntry extends Entry {
    public record RoutingTableEntry(NodeEntry DST, ConnectionEntry RT) implements ExportableToJson {
        public RoutingTableEntry(JsonObject object) {
            this(
                    DendroNetwork.DATA_HANDLER.getNode(object.getDecimal("dst").decimal.longValue()),
                    DendroNetwork.DATA_HANDLER.getConnection(object.getDecimal("rt").decimal.longValue())
            );
        }

        @Override
        public JsonItem export() {
            JsonObject object = new JsonObject();
            object.put("dst", new JsonDecimal(DST.getUUID()));
            object.put("rt", new JsonDecimal(RT.getUUID()));
            return object;
        }

        @Override
        public String toString() {
            return "{" + DST.getName() + ", " + RT.getName() + "}";
        }
    }

    private final String NAME;
    private final ArrayList<RoutingTableEntry> ROUTING_TABLE;
    private final ArrayList<ConnectionEntry> CONNECTIONS;

    public NodeEntry(String name) {
        super();
        NAME = name;
        ROUTING_TABLE = new ArrayList<>();
        CONNECTIONS = new ArrayList<>();
    }

    public NodeEntry(JsonObject object, ImportHandler.ImportMode mode) {
        super(object, mode);
        NAME = object.getString("name").getString();
        ROUTING_TABLE = new ArrayList<>();
        object.getArray("rt-tbl").getObjectArray().forEach(obj -> ROUTING_TABLE.add(new RoutingTableEntry(obj)));
        CONNECTIONS = new ArrayList<>();
    }

    public String getName() {
        return NAME;
    }

    public ConnectionEntry route(NodeEntry dst) throws LoopBackException {
        if (dst.equals(this)) {
            throw new LoopBackException();
        } else {
            for (RoutingTableEntry entry : ROUTING_TABLE) {
                if (entry.DST.equals(dst)) {
                    return entry.RT;
                }
            }
            return null;
            //TODO code to find shortest route if not cached
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
        for (RoutingTableEntry entry : ROUTING_TABLE) {
            array.add(entry.export());
        }
        object.put("rt-tbl", array);
        return object;
    }

    @Override
    public String toFlatString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getUUID()).append("\t{")
                .append(NAME);
        ROUTING_TABLE.forEach(entry -> builder.append(entry).append(", "));
        builder.append("}");
        return builder.toString().replace(", }", "}");
    }

    @Override
    public String toString() {
        return NAME;
    }
}
