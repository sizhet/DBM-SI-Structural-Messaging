package com.dbm.structure_messaging.decode;

import com.dbm.structure_messaging.evidence.EvidenceChain;
import com.dbm.structure_messaging.model.WordToken;

import java.util.List;

public final class DecodeResult {
    private final List<WordToken> tokens;
    private final EvidenceChain evidence;

    public DecodeResult(List<WordToken> tokens, EvidenceChain evidence) {
        this.tokens = tokens;
        this.evidence = evidence;
    }

    public List<WordToken> tokens() { return tokens; }
    public EvidenceChain evidence() { return evidence; }

    public String asText() {
        StringBuilder sb = new StringBuilder();
        for (WordToken t : tokens) sb.append(t.text());
        return sb.toString();
    }
}
