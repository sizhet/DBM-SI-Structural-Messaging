package com.dbm.structure_messaging.benchmark;

import java.nio.charset.StandardCharsets;

public final class StructuralMessage {

    public final String kind;
    public final String payload;
    public final String snapshotId;
    public final Integer pointerIndex;

    StructuralMessage(String k, String p, String s, Integer pi) {
        kind = k;
        payload = p;
        snapshotId = s;
        pointerIndex = pi;
    }

    public int bytes() {
        String s =
                kind + "|" +
                (payload == null ? "" : payload) + "|" +
                (snapshotId == null ? "" : snapshotId) + "|" +
                (pointerIndex == null ? "" : pointerIndex);

        return s.getBytes(StandardCharsets.UTF_8).length;
    }
}
