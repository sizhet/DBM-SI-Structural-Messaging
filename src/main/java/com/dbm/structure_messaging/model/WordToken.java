package com.dbm.structure_messaging.model;

import java.util.Objects;

/** Word/token in the pre-agreed vocabulary. */
public final class WordToken {
    private final String text;

    public WordToken(String text) {
        if (text == null || text.trim().isEmpty()) throw new IllegalArgumentException("text");
        this.text = text;
    }

    public String text() { return text; }

    @Override public String toString() { return text; }

    @Override public boolean equals(Object o) {
        return (o instanceof WordToken) && Objects.equals(text, ((WordToken)o).text);
    }
    @Override public int hashCode() { return Objects.hash(text); }
}
