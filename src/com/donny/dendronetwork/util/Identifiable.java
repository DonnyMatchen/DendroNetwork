package com.donny.dendronetwork.util;

import com.donny.dendronetwork.json.JsonItem;

public interface Identifiable<E extends JsonItem> {
    E getIdentifier();
}
