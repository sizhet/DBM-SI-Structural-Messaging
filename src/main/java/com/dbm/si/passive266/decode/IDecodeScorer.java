package com.dbm.si.passive266.decode;

public interface IDecodeScorer {
    /**
     * Return a higher-is-better score. Must be deterministic.
     * Score can combine: token coverage, structural invariants, evidence stability, etc.
     */
    double score(ScoreContext ctx);
}
