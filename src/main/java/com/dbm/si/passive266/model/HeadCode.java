package com.dbm.si.passive266.model;

import java.util.Objects;

public final class HeadCode {
    private final int bits;      // packed bits, e.g., for H=2, range [0..3]
    private final int bitWidth;  // H

    public HeadCode(int bits, int bitWidth) {
        if (bitWidth <= 0 || bitWidth > 30) throw new IllegalArgumentException("bitWidth");
        int max = (1 << bitWidth) - 1;
        if (bits < 0 || bits > max) throw new IllegalArgumentException("bits out of range");
        this.bits = bits;
        this.bitWidth = bitWidth;
    }

    public int bits() { return bits; }
    public int bitWidth() { return bitWidth; }

    public String toBitString() {
        String s = Integer.toBinaryString(bits);
        while (s.length() < bitWidth) s = "0" + s;
        return s;
    }

    @Override public String toString() { return toBitString(); }

    @Override public boolean equals(Object o) {
        if (!(o instanceof HeadCode)) return false;
        HeadCode other = (HeadCode) o;
        return bits == other.bits && bitWidth == other.bitWidth;
    }
    @Override public int hashCode() { return Objects.hash(bits, bitWidth); }
}
