package com.dbm.structure_messaging.stream;

import com.dbm.structure_messaging.model.HeadCode;

public final class BitStreamReader {
    private final BitStream stream;

    public BitStreamReader(BitStream stream) {
        if (stream == null) throw new IllegalArgumentException("stream");
        this.stream = stream;
    }

    /** Read bitWidth bits at position pos, MSB-first, and pack to int. */
    public HeadCode readHead(long pos, int bitWidth) {
        int v = 0;
        for (int i = 0; i < bitWidth; i++) {
            v = (v << 1) | (stream.bitAt(pos + i) & 1);
        }
        return new HeadCode(v, bitWidth);
    }
}
