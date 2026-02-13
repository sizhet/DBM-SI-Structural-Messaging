package com.dbm.si.passive266.table;

import com.dbm.si.passive266.model.HeadCode;

import java.util.*;

public final class InMemoryWordEncodingTable implements WordEncodingTable {
    private final int headBitWidth;
    private final int maxTail;
    private final Map<Integer, List<EncodingEntry>> map = new HashMap<>();

    public InMemoryWordEncodingTable(int headBitWidth, int maxTail) {
        if (headBitWidth <= 0 || headBitWidth > 30) throw new IllegalArgumentException("headBitWidth");
        if (maxTail < 0) throw new IllegalArgumentException("maxTail");
        this.headBitWidth = headBitWidth;
        this.maxTail = maxTail;
    }

    public InMemoryWordEncodingTable put(int headBitsPacked, EncodingEntry... entries) {
        map.put(headBitsPacked, Collections.unmodifiableList(Arrays.asList(entries)));
        return this;
    }

    @Override
    public List<EncodingEntry> lookup(HeadCode head) {
        if (head.bitWidth() != headBitWidth) return Collections.emptyList();
        return map.getOrDefault(head.bits(), Collections.emptyList());
    }

    @Override public int headBitWidth() { return headBitWidth; }
    @Override public int maxTailLength() { return maxTail; }
}
