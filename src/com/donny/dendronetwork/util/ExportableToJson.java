package com.donny.dendronetwork.util;

import com.donny.dendronetwork.json.JsonFormattingException;
import com.donny.dendronetwork.json.JsonItem;

public interface ExportableToJson {
    JsonItem export() throws JsonFormattingException;
}
