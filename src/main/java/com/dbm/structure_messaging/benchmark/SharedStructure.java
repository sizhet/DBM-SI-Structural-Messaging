package com.dbm.structure_messaging.benchmark;

import java.util.HashMap;
import java.util.Map;

public final class SharedStructure {

    private final Map<String, WorkflowState> snapshots = new HashMap<>();
    private StructuralIndex index;

    public void putSnapshot(String id, WorkflowState s) {
        snapshots.put(id, s.copy());
    }

    public WorkflowState getSnapshot(String id) {
        return snapshots.get(id).copy();
    }

    public StructuralIndex getIndex() { return index; }

    public void setIndex(StructuralIndex i) { index = i; }
}
