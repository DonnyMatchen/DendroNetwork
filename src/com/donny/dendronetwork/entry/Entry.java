package com.donny.dendronetwork.entry;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.data.ImportHandler;
import com.donny.dendronetwork.json.JsonDecimal;
import com.donny.dendronetwork.json.JsonFormattingException;
import com.donny.dendronetwork.json.JsonObject;
import com.donny.dendronetwork.util.ExportableToJson;
import com.donny.dendronetwork.util.Identifiable;

import java.io.Serializable;

public abstract class Entry implements ExportableToJson, Identifiable<JsonDecimal>, Serializable {
    private final long UUID;
    public boolean clashing;

    public Entry() {
        UUID = DendroNetwork.UUID_HANDLER.generateUUID();
    }

    public Entry(JsonObject object, ImportHandler.ImportMode mode) {
        if (object.containsKey("_uuid")) {
            long candidate = object.getDecimal("_uuid").decimal.longValue();
            if (DendroNetwork.UUID_HANDLER.UUIDS.contains(candidate)) {
                DendroNetwork.LOG_HANDLER.warn(getClass(), "Clashing UUID: " + candidate);
                clashing = true;
                if (mode == ImportHandler.ImportMode.OVERWRITE) {
                    UUID = candidate;
                } else {
                    UUID = DendroNetwork.UUID_HANDLER.generateUUID();
                }
            } else {
                UUID = candidate;
                DendroNetwork.UUID_HANDLER.UUIDS.add(UUID);
            }
        } else {
            UUID = DendroNetwork.UUID_HANDLER.generateUUID();
        }
    }

    public static Entry get(EntryType type, JsonObject obj) {
        return switch (type) {
            case NODE -> new NodeEntry(obj, ImportHandler.ImportMode.KEEP);
            case CONNECTION -> new ConnectionEntry(obj, ImportHandler.ImportMode.KEEP);
        };
    }

    public long getUUID() {
        return UUID;
    }

    public JsonObject export() throws JsonFormattingException {
        JsonObject object = new JsonObject();
        object.put("_uuid", new JsonDecimal(UUID));
        return object;
    }

    public abstract String toFlatString();

    @Override
    public JsonDecimal getIdentifier() {
        return new JsonDecimal(UUID);
    }
}
