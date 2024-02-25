package com.donny.dendronetwork.util;

import com.donny.dendroroot.json.JsonFormattingException;
import com.donny.dendroroot.json.JsonItem;

public interface Identifiable<E> {
    E getIdentifier();
}
