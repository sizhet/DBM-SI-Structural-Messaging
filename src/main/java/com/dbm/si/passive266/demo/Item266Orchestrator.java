package com.dbm.si.passive266.demo;

import com.dbm.si.passive266.decode.PermutationTreeDecoderV2;
import com.dbm.si.passive266.decode.PermutationTreeDecoderV2.DecodeHints;
import com.dbm.si.passive266.decode.PermutationTreeDecoderV2.DecodeRunResult;
import com.dbm.si.passive266.decode.StableKeyTracker;
import com.dbm.si.passive266.model.WordToken;
import com.dbm.si.passive266.stream.BitStreamReader;
import com.dbm.si.passive266.vote.ConvergenceChecker266;
import com.dbm.si.passive266.vote.VoteAggregator266;

import java.util.List;
import java.util.Set;

public final class Item266Orchestrator {
    private final PermutationTreeDecoderV2 decoder;
    private final VoteAggregator266 vote = new VoteAggregator266();
    private final ConvergenceChecker266 conv = new ConvergenceChecker266();

    public Item266Orchestrator(PermutationTreeDecoderV2 decoder) {
        this.decoder = decoder;
    }

    public String runMultiple(BitStreamReader[] runs, int topK, double evidenceWeight, int stableRuns, Set<WordToken> desiredSet) {
        StableKeyTracker tracker = new StableKeyTracker();

        for (int i = 0; i < runs.length; i++) {
            DecodeHints hints = new DecodeHints(desiredSet, tracker);
            DecodeRunResult rr = decoder.decode(runs[i], 0, hints);

            vote.addRun(rr.acceptedCandidates, topK, evidenceWeight);

            ConvergenceChecker266.ConvergenceDecision dec = conv.check(vote.history(), stableRuns);
            if (dec.converged) {
                List<VoteAggregator266.VoteScore> top = vote.topN(1);
                return top.isEmpty() ? null : top.get(0).text;
            }
        }

        List<VoteAggregator266.VoteScore> top = vote.topN(1);
        return top.isEmpty() ? null : top.get(0).text;
    }
}
