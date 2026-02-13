package com.dbm.structure_messaging.table;

import com.dbm.structure_messaging.model.HeadCode;

import java.util.List;

public interface WordEncodingTable {
    /** Return candidates for a head code. Empty means dead-end. */
    List<EncodingEntry> lookup(HeadCode head);

    /** Head bit width H. */
    int headBitWidth();

    /** Optional: global max tail for sanity bounds. */
    int maxTailLength();
}
