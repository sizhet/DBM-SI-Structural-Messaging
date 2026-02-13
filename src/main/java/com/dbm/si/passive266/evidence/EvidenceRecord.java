package com.dbm.si.passive266.evidence;

public final class EvidenceRecord {
    private final String key;
    private final String value;

    public EvidenceRecord(String key, String value) {
        if (key == null || key.trim().isEmpty()) throw new IllegalArgumentException("key");
        this.key = key;
        this.value = value == null ? "" : value;
    }

    public String key() { return key; }
    public String value() { return value; }

    @Override public String toString() { return key + "=" + value; }
}
