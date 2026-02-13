package com.dbm.si.passive266;

import com.dbm.si.passive266.decode.DecodeResult;
import com.dbm.si.passive266.evidence.EvidenceChain;
import com.dbm.si.passive266.model.WordToken;
import com.dbm.si.passive266.vote.ConvergenceChecker266;
import com.dbm.si.passive266.vote.VoteAggregator266;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class VoteConvergenceTest {

    @Test
    public void testConvergenceTopTextStable() {
        VoteAggregator266 vote = new VoteAggregator266();
        ConvergenceChecker266 conv = new ConvergenceChecker266();

        // fabricate candidates: top is always "奥博"
        DecodeResult top = new DecodeResult(Arrays.asList(new WordToken("奥"), new WordToken("博")),
                new EvidenceChain().add("head", "01").add("token", "奥"));

        for (int i = 0; i < 3; i++) {
            vote.addRun(Arrays.asList(top), 1, 0.0);
        }

        ConvergenceChecker266.ConvergenceDecision d = conv.check(vote.history(), 3);
        Assert.assertTrue(d.converged);
        Assert.assertTrue(d.reason.startsWith("TOP_TEXT_STABLE"));
    }
}
