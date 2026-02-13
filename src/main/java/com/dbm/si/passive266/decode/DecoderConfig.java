package com.dbm.si.passive266.decode;

public final class DecoderConfig {
    public int maxTokens = 16;          // safety bound
    public long maxNodesExpanded = 50_000;
    public int beamWidth = 256;         // simple beam cap
}
