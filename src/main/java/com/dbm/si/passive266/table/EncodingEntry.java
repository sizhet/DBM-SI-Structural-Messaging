package com.dbm.si.passive266.table;

import com.dbm.si.passive266.model.WordToken;

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
