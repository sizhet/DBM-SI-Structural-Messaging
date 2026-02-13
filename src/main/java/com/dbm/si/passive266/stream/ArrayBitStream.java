package com.dbm.si.passive266.stream;

public final class ArrayBitStream implements BitStream {
    private final int[] bits;

    public ArrayBitStream(int[] bits) {
        if (bits == null) throw new IllegalArgumentException("bits");
        this.bits = bits.clone();
        for (int b : this.bits) if (b != 0 && b != 1) throw new IllegalArgumentException("bit must be 0/1");
    }

    @Override
    public int bitAt(long index) {
        if (index < 0 || index >= bits.length) return 0; // safe default; could throw instead
        return bits[(int) index];
    }

    @Override
    public long length() { return bits.length; }
}
