package com.dbm.structure_messaging.benchmark;

final class BenchmarkResult {
    final String level;
    final int bytes;
    final boolean ok;

    BenchmarkResult(String l, int b, boolean o) {
        level = l;
        bytes = b;
        ok = o;
    }
}
