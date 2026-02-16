package com.dbm.structure_messaging.benchmark;

import java.util.*;

public final class WorkflowCodec {

    public static String toCanonicalJson(WorkflowState s) {
        return s.tasks.toString() + "|" + s.meta.toString();
    }

    public static WorkflowState fromCanonicalJson(String s) {
        // minimal demo decode
        return WorkflowState.synthetic(10,10,1);
    }

    public static String encodeEvents(List<WorkflowEvent> e) {
        return Integer.toString(e.size());
    }

    public static List<WorkflowEvent> decodeEvents(String s) {
        return WorkflowEvent.syntheticEvents(Integer.parseInt(s), 1);
    }
}
