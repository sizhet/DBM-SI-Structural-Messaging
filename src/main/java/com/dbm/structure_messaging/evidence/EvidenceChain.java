package com.dbm.structure_messaging.evidence;

import java.util.ArrayList;
import java.util.List;

public final class EvidenceChain {
    private final List<EvidenceRecord> records = new ArrayList<>();

    public EvidenceChain add(String key, String value) {
        records.add(new EvidenceRecord(key, value));
        return this;
    }

    public List<EvidenceRecord> records() {
        //return Collections.unmodifiableList(records);  // 2026 02 12 -- caller requires mutable list
        return records;
    }

    @Override public String toString() { return records.toString(); }
}
