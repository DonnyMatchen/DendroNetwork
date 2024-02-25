package com.donny.dendronetwork.data.lnetwork;

import com.donny.dendronetwork.instance.ProgramInstance;
import com.donny.dendroroot.json.*;
import com.donny.dendroroot.util.ExportableToJson;

import java.math.BigDecimal;

public class LConnection implements ExportableToJson {
    private final BigDecimal COST;
    private final LNetwork NETWORK;
    private final LNode A, B;
    private boolean active;

    public LConnection(BigDecimal cost, LNetwork network, LNode a, LNode b, ProgramInstance instance) {
        NETWORK = network;
        COST = cost;
        A = a;
        B = b;
        active = true;
        instance.ALL_CONNECTIONS.add(this);
    }

    public LConnection(LNetwork network, LNode a, LNode b, ProgramInstance instance) {
        this(BigDecimal.ONE, network, a, b, instance);
    }

    public BigDecimal getCost() {
        return COST;
    }

    public LNetwork getNetwork() {
        return NETWORK;
    }

    public String getNetworkName() {
        return NETWORK.getName();
    }

    public LNode[] getNodes() {
        return new LNode[]{A, B};
    }

    public String[] getNodeNames() {
        return new String[]{A.getName(), B.getName()};
    }

    public boolean isActive() {
        return active;
    }

    public boolean toggle() {
        active = !active;
        return active;
    }

    public LNode traverse(LNode node) {
        if (A == node) {
            return B;
        } else if (B == node) {
            return A;
        } else {
            return null;
        }
    }

    @Override
    public JsonItem export() throws JsonFormattingException {
        JsonObject object = new JsonObject();
        object.put("n", new JsonString(NETWORK.getName()));
        object.put("a", new JsonString(A.getIdentifier()));
        object.put("b", new JsonString(B.getIdentifier()));
        object.put("c", new JsonDecimal(COST));
        return object;
    }
}
