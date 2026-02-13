package com.dbm.structure_messaging.stream;

/**
 * Shared random bit stream abstraction.
 * This is NOT "signal + noise"; it's just a bit stream y1,y2,y3,...
 */
public interface BitStream {
    /** return bit at index (0-based). Must be 0 or 1. */
    int bitAt(long index);

    /** optional: known length; return -1 if unbounded */
    default long length() { return -1L; }
}
