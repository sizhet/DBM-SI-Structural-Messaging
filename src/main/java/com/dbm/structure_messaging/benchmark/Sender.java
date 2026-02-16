package com.dbm.structure_messaging.benchmark;

import java.util.List;

public final class Sender {

    public static StructuralMessage build(
            CollapseBenchmarkRunner.Level level,
            String snapshotId,
            WorkflowState target,
            List<WorkflowEvent> events,
            BenchmarkConfig cfg) {

        switch (level) {
            case S0:
                return new StructuralMessage(
                        "FULL",
                        WorkflowCodec.toCanonicalJson(target),
                        null,
                        null);

            case S1:
                return new StructuralMessage(
                        "SCHEMA",
                        WorkflowCodec.toCanonicalJson(target),
                        null,
                        null);

            case S2:
                return new StructuralMessage(
                        "SNAPSHOT_DIFF",
                        WorkflowCodec.encodeEvents(events),
                        snapshotId,
                        null);

            case S3:
                return new StructuralMessage(
                        "POINTER",
                        null,
                        snapshotId,
                        events.size() - 1);
        }

        throw new IllegalStateException();
    }
}
