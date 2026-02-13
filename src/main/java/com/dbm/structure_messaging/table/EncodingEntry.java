package com.dbm.structure_messaging.table;

import com.dbm.structure_messaging.model.WordToken;

public final class EncodingEntry {
    private final WordToken token;
    private final int tailLength;

    public EncodingEntry(WordToken token, int tailLength) {
        if (token == null) throw new IllegalArgumentException("token");
        if (tailLength < 0) throw new IllegalArgumentException("tailLength");
        this.token = token;
        this.tailLength = tailLength;
    }

    public WordToken token() { return token; }
    public int tailLength() { return tailLength; }
}
