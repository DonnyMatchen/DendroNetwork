package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.entry.Entry;
import com.donny.dendronetwork.entry.EntryType;
import com.donny.dendronetwork.json.JsonArray;
import com.donny.dendronetwork.json.JsonFormattingException;
import com.donny.dendronetwork.json.JsonItem;
import com.donny.dendronetwork.json.JsonObject;

import java.io.File;
import java.util.ArrayList;

public class DataSet<E extends Entry> {
    private final EntryType TYPE;
    private final File ARCHIVE;
    private final ArrayList<E> TABLE;
    private boolean emptyDueToDeletion = false;

    public DataSet(String name, EntryType type) {
        TYPE = type;
        ARCHIVE = new File(DendroNetwork.data.getPath() + File.separator + "Entries" + File.separator + name + ".json");
        TABLE = new ArrayList<>();
    }

    public void reload() throws JsonFormattingException {
        TABLE.clear();
        if (ARCHIVE.exists()) {
            String raw = DendroNetwork.FILE_HANDLER.read(ARCHIVE);
            if (raw.contains("passwd")) {
                raw = raw.substring(6);
            }
            JsonArray entries = (JsonArray) JsonItem.sanitizeDigest(raw);
            for (JsonObject obj : entries.getObjectArray()) {
                TABLE.add((E) Entry.get(TYPE, obj));
            }
        }
    }

    public void save() {
        if (!TABLE.isEmpty()) {
            long uuid = 0;
            try {
                JsonArray array = new JsonArray();
                for (E entry : TABLE) {
                    uuid = entry.getUUID();
                    array.add(entry.export());
                }
                DendroNetwork.FILE_HANDLER.write(ARCHIVE, array.toString());
            } catch (JsonFormattingException ex) {
                DendroNetwork.LOG_HANDLER.error(getClass(), "Entry " + uuid + " Failed formatting!");
            }
        } else {
            if (ARCHIVE.exists()) {
                DendroNetwork.FILE_HANDLER.delete(ARCHIVE);
            }
        }
    }

    public boolean add(E element) {
        emptyDueToDeletion = false;
        return TABLE.add(element);
    }

    public boolean remove(E element) {
        boolean removed = TABLE.remove(element);
        if (TABLE.isEmpty()) {
            emptyDueToDeletion = true;
        }
        return removed;
    }

    public ArrayList<E> read() throws JsonFormattingException {
        if (TABLE.isEmpty() && !emptyDueToDeletion) {
            reload();
        }
        return new ArrayList<>(TABLE);
    }
}
