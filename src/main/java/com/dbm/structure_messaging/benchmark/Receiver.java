package com.dbm.structure_messaging.benchmark;

import java.util.List;

public final class Receiver {

    public static WorkflowState reconstruct(
            CollapseBenchmarkRunner.Level level,
            SharedStructure shared,
            StructuralMessage msg) {

        switch (level) {

            case S0:
            case S1:
                return WorkflowCodec.fromCanonicalJson(msg.payload);

            case S2: {
                WorkflowState s = shared.getSnapshot(msg.snapshotId);
                List<WorkflowEvent> events =
                        WorkflowCodec.decodeEvents(msg.payload);
                for (WorkflowEvent e : events) e.applyTo(s);
                return s;
            }

            case S3: {
                WorkflowState s = shared.getSnapshot(msg.snapshotId);
                StructuralIndex idx = shared.getIndex();
                for (int i = 0; i <= msg.pointerIndex; i++) {
                    idx.getEvent(i).applyTo(s);
                }
                return s;
            }
        }

        throw new IllegalStateException();
    }
}
