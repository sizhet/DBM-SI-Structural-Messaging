package com.dbm.si.passive266.vote;

import java.util.List;
import java.util.Objects;

public final class ConvergenceChecker266 {

    public static final class ConvergenceDecision {
        public final boolean converged;
        public final String reason;
        public ConvergenceDecision(boolean converged, String reason) {
            this.converged = converged; this.reason = reason;
        }
        public static ConvergenceDecision yes(String r){ return new ConvergenceDecision(true, r); }
        public static ConvergenceDecision no(){ return new ConvergenceDecision(false, ""); }
    }

    /**
     * DBM-flavored convergence:
     * - topText stable for last K runs
     * - and topEvidenceKeySet stable (optional)
     */
    public ConvergenceDecision check(List<VoteAggregator266.VoteSnapshot> history, int stableRuns) {
        if (history.size() < stableRuns) return ConvergenceDecision.no();

        String ref = history.get(history.size() - 1).topText;
        if (ref == null) return ConvergenceDecision.no();

        for (int i = history.size() - stableRuns; i < history.size(); i++) {
            if (!Objects.equals(ref, history.get(i).topText)) return ConvergenceDecision.no();
        }
        return ConvergenceDecision.yes("TOP_TEXT_STABLE_" + stableRuns);
    }
}
