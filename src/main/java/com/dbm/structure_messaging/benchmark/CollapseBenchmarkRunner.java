package com.dbm.structure_messaging.benchmark;

import java.util.ArrayList;
import java.util.List;

public final class CollapseBenchmarkRunner {

    public enum Level { S0, S1, S2, S3 }

    public static void main(String[] args) {

        BenchmarkConfig cfg = BenchmarkConfig.defaults();

        WorkflowState baseline =
                WorkflowState.synthetic(cfg.taskCount, cfg.metaPairs, cfg.seed);

        List<WorkflowEvent> events =
                WorkflowEvent.syntheticEvents(cfg.eventCount, cfg.seed);

        WorkflowState target = baseline.copy();
        for (WorkflowEvent e : events) e.applyTo(target);

        SharedStructure shared = new SharedStructure();
        String snapshotId = "snap0";
        shared.putSnapshot(snapshotId, baseline);

        StructuralIndex index = new StructuralIndex();
        for (int i = 0; i < events.size(); i++) {
            index.putEvent(i, events.get(i));
        }
        shared.setIndex(index);

        List<BenchmarkResult> results = new ArrayList<>();

        for (Level level : Level.values()) {
            StructuralMessage msg =
                    Sender.build(level, snapshotId, target, events, cfg);

            WorkflowState reconstructed =
                    Receiver.reconstruct(level, shared, msg);

            String targetHash =
                    InvariantHash.sha256Hex(WorkflowCodec.toCanonicalJson(target));

            String reconHash =
                    InvariantHash.sha256Hex(WorkflowCodec.toCanonicalJson(reconstructed));

            boolean ok = targetHash.equals(reconHash);

            results.add(new BenchmarkResult(level.name(), msg.bytes(), ok));
        }

        printMarkdownTable(results);
    }

    private static void printMarkdownTable(List<BenchmarkResult> rows) {
        int base = rows.get(0).bytes;

        System.out.println();
        System.out.println("### Collapse Benchmark Result");
        System.out.println();
        System.out.println("| Level | Bytes | Compression(vs S0) | OK |");
        System.out.println("|------|------:|-------------------:|:--:|");

        for (BenchmarkResult r : rows) {
            double cr = (double) base / r.bytes;
            System.out.printf("| %s | %d | %.1fx | %s |%n",
                    r.level, r.bytes, cr, r.ok ? "✓" : "✗");
        }
    }
}
/*
### Collapse Benchmark Result

| Level | Bytes | Compression(vs S0) | OK |
|------|------:|-------------------:|:--:|
| S0 | 16404 | 1.0x | ✗ |
| S1 | 16406 | 1.0x | ✗ |
| S2 | 23 | 713.2x | ✗ |
| S3 | 17 | 964.9x | ✓ |
 */