package com.dbm.structure_messaging.decode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks stability of "evidence keys" across expansions.
 * For ITEM#266, this is a lightweight placeholder that can be
 * extended to include invariant hashes or evidence key sets.
 */
public final class StableKeyTracker {
    private final Map<String, Integer> keyCounts = new HashMap<>();

    public void observeKey(String key) {
        if (key == null || key.isEmpty()) return;
        keyCounts.put(key, keyCounts.getOrDefault(key, 0) + 1);
    }

    /** higher means more stable evidence so far */
    public double stabilityScore() {
        if (keyCounts.isEmpty()) return 0.0;
        int max = 0;
        int sum = 0;
        for (int c : keyCounts.values()) { sum += c; if (c > max) max = c; }
        return (double) max / (double) sum;
    }

    public Map<String, Integer> snapshotCounts() {
        return Collections.unmodifiableMap(new HashMap<>(keyCounts));
    }
}
