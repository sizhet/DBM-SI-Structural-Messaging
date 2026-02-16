package com.dbm.structure_messaging.benchmark;

import java.util.*;

public interface WorkflowEvent {

    void applyTo(WorkflowState s);

    static List<WorkflowEvent> syntheticEvents(int n, long seed) {
        Random r = new Random(seed);
        List<WorkflowEvent> out = new ArrayList<>();

        for (int i=0;i<n;i++) {
            int idx = r.nextInt(200);
            out.add(s -> s.tasks.put("task-"+idx,"DONE"));
        }

        return out;
    }
}
