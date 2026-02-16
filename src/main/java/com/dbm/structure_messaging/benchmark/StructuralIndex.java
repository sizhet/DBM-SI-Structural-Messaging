package com.dbm.structure_messaging.benchmark;

import java.util.HashMap;
import java.util.Map;

public final class StructuralIndex {

    private final Map<Integer, WorkflowEvent> events = new HashMap<>();

    public void putEvent(int i, WorkflowEvent e) {
        events.put(i, e);
    }

    public WorkflowEvent getEvent(int i) {
        return events.get(i);
    }
}
