package com.dbm.structure_messaging.benchmark;

public final class BenchmarkConfig {

    public final int taskCount;
    public final int metaPairs;
    public final int eventCount;
    public final long seed;

    private BenchmarkConfig(int t, int m, int e, long s) {
        taskCount = t;
        metaPairs = m;
        eventCount = e;
        seed = s;
    }

    public static BenchmarkConfig defaults() {
        return new BenchmarkConfig(800, 300, 60, 42);
    }
}
