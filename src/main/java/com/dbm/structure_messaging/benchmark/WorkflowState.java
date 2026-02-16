package com.dbm.structure_messaging.benchmark;

import java.util.*;

public final class WorkflowState {

    public final Map<String,String> tasks;
    public final Map<String,String> meta;

    WorkflowState(Map<String,String> t, Map<String,String> m) {
        tasks = t;
        meta = m;
    }

    public static WorkflowState synthetic(int n, int m, long seed) {
        Map<String,String> t = new LinkedHashMap<>();
        for (int i=0;i<n;i++) t.put("task-"+i,"READY");

        Map<String,String> meta = new LinkedHashMap<>();
        Random r = new Random(seed);
        for (int i=0;i<m;i++) meta.put("k"+i,"v"+r.nextInt(100000));

        return new WorkflowState(t,meta);
    }

    public WorkflowState copy() {
        return new WorkflowState(
                new LinkedHashMap<>(tasks),
                new LinkedHashMap<>(meta));
    }
}
