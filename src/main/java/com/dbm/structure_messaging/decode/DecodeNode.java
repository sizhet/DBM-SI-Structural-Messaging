package com.dbm.structure_messaging.decode;

import com.dbm.structure_messaging.evidence.EvidenceChain;
import com.dbm.structure_messaging.model.WordToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DecodeNode {
    private final long pos;
    private final int state;
    private final List<WordToken> path;
    private final EvidenceChain evidence;

    public DecodeNode(long pos, int state, List<WordToken> path, EvidenceChain evidence) {
        this.pos = pos;
        this.state = state;
        this.path = path;
        this.evidence = evidence;
    }

    public long pos() { return pos; }
    public int state() { return state; }
    public List<WordToken> path() { return Collections.unmodifiableList(path); }
    public EvidenceChain evidence() { return evidence; }

    public DecodeNode extend(long nextPos, int nextState, WordToken token, EvidenceChain nextEvidence) {
        List<WordToken> np = new ArrayList<>(path);
        np.add(token);
        return new DecodeNode(nextPos, nextState, np, nextEvidence);
    }
}
