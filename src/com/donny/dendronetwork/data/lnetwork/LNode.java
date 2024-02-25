package com.donny.dendronetwork.data.lnetwork;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.lnetwork.exceptions.DestinationUnreachableException;
import com.donny.dendronetwork.data.lnetwork.exceptions.LoopBackException;
import com.donny.dendronetwork.instance.ProgramInstance;
import com.donny.dendronetwork.util.Identifiable;
import com.donny.dendroroot.json.JsonFormattingException;
import com.donny.dendroroot.json.JsonObject;
import com.donny.dendroroot.json.JsonString;
import com.donny.dendroroot.util.ExportableToJson;

import java.util.ArrayList;
import java.util.HashMap;

public class LNode implements Comparable<LNode>, ExportableToJson, Identifiable<String> {
    private final LNetwork NETWORK;
    private final String NAME;
    protected final ArrayList<LConnection> CONNECTIONS;
    protected final HashMap<String, String> ROUTING_TABLE;

    private String description;

    protected LNode(String name, String desc, LNetwork network, ProgramInstance instance) {
        if(name.contains(".")) {
            throw new IllegalArgumentException("Node names cannot have a period!");
        }
        NAME = name;
        description = desc;
        NETWORK = network;
        CONNECTIONS = new ArrayList<>();
        ROUTING_TABLE = new HashMap<>();
        instance.ALL_NODES.put(network + "." + name, this);
    }

    protected LNode(String name, LNetwork network, ProgramInstance instance) {
        this(name, "", network, instance);
    }

    public String getName() {
        return NAME;
    }

    public LNetwork getNetwork() {
        return NETWORK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public void loadRoutes(JsonObject object) {
        for(String s : object.getFields()) {
            ROUTING_TABLE.put(s, object.getString(s).getString());
        }
    }

    protected boolean connect(LConnection con) {
        LConnection old = getConnection(con.traverse(this));
        if(old != null) {
            CONNECTIONS.remove(old);
        }
        return CONNECTIONS.add(con);
    }

    public boolean connectedTo(LNode node) {
        for(LConnection con : CONNECTIONS) {
            if(con.traverse(this) == node) {
                return true;
            }
        }
        return false;
    }

    public LConnection getConnection(LNode node) {
        for(LConnection con : CONNECTIONS) {
            if(con.traverse(this) == node) {
                return con;
            }
        }
        return null;
    }

    public LConnection getConnection(String nodId) {
        for(LConnection con : CONNECTIONS) {
            if(con.traverse(this).NAME.equals(nodId)) {
                return con;
            }
        }
        return null;
    }

    public ArrayList<LConnection> getConnections() {
        return new ArrayList<>(CONNECTIONS);
    }

    public void flushCache() {
        ROUTING_TABLE.clear();
    }

    public LConnection route(LNode dst) throws LoopBackException, DestinationUnreachableException {
        if (dst.equals(this)) {
            throw new LoopBackException();
        } else {
            ArrayList<LConnection> list;
            if (!ROUTING_TABLE.containsKey(dst.NAME)) {
                NETWORK.rebuildRoutingTable(this);
                if(!ROUTING_TABLE.containsKey(dst.NAME)) {
                    throw new DestinationUnreachableException();
                }
            }
            return getConnection(ROUTING_TABLE.get(dst.NAME));
        }
    }

    @Override
    public String getIdentifier() {
        return NETWORK.getName() + "." + NAME;
    }

    public JsonObject exportRoutes() throws JsonFormattingException {
        JsonObject routes = new JsonObject();
        for(String s : ROUTING_TABLE.keySet()) {
            routes.put(s, new JsonString(ROUTING_TABLE.get(s)));
        }
        return routes;
    }

    public void addRoute(LNode destination, LNode nextHop) {
        ROUTING_TABLE.put(destination.NAME, nextHop.NAME);
    }

    @Override
    public JsonObject export() throws JsonFormattingException {
        JsonObject object = new JsonObject();
        object.put("n", new JsonString(NETWORK.getName()));
        object.put("i", new JsonString(NAME));
        object.put("d", new JsonString(description));
        object.put("r", exportRoutes());
        return object;
    }

    @Override
    public int compareTo(LNode node) {
        return (NETWORK.getName() + "." + NAME).compareTo(node.NETWORK.getName() + "." + node.NAME);
    }
}
