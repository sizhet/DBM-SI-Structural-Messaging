package com.dbm.si.passive266.decode;

public final class BeamCandidate {
    public final DecodeNode node;
    public final double score;

    public BeamCandidate(DecodeNode node, double score) {
        this.node = node;
        this.score = score;
    }
}
