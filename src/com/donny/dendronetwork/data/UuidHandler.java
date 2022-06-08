package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;

import java.security.SecureRandom;
import java.util.ArrayList;

public class UuidHandler {
    public final ArrayList<Long> UUIDS;

    public UuidHandler() {
        UUIDS = new ArrayList<>();
        DendroNetwork.LOG_HANDLER.trace(getClass(), "UuidHandler Initiated");
    }

    public long generateUUID() {
        SecureRandom rand = new SecureRandom();
        boolean flag = true;
        long candidate = 0;
        while (flag) {
            candidate = rand.nextLong();
            flag = check(candidate);
        }
        UUIDS.add(candidate);
        return candidate;
    }

    public boolean check(long l) {
        if (l == 0) {
            return true;
        }
        return UUIDS.contains(l);
    }
}
