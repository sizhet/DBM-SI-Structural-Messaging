package com.dbm.structure_messaging.vote;

import com.dbm.structure_messaging.decode.DecodeResult;
import com.dbm.structure_messaging.evidence.EvidenceRecord;

import java.util.*;

public final class VoteAggregator266 {

    public static final class VoteSnapshot {
        public final int runIndex;
        public final String topText;
        public final Set<String> topEvidenceKeys;

        public VoteSnapshot(int runIndex, String topText, Set<String> topEvidenceKeys) {
            this.runIndex = runIndex;
            this.topText = topText;
            this.topEvidenceKeys = topEvidenceKeys == null ? Collections.emptySet() : Collections.unmodifiableSet(topEvidenceKeys);
        }
    }

    public static final class VoteScore {
        public final String text;
        public final double score;
        public final int runsContributed;
        public VoteScore(String text, double score, int runsContributed) {
            this.text = text; this.score = score; this.runsContributed = runsContributed;
        }
        @Override public String toString() { return text + " score=" + score + " runs=" + runsContributed; }
    }

    private final Map<String, Double> textScore = new HashMap<>();
    private final Map<String, Integer> textRuns = new HashMap<>();
    private final List<VoteSnapshot> history = new ArrayList<>();

    private int runIndex = 0;

    /** Add top-k candidates from one decoding run. */
    public void addRun(List<DecodeResult> candidates, int topK, double evidenceWeight) {
        runIndex++;

        if (candidates == null || candidates.isEmpty()) {
            history.add(new VoteSnapshot(runIndex, null, Collections.emptySet()));
            return;
        }

        int k = Math.max(1, Math.min(topK, candidates.size()));

        // score: rank-based + evidence-key stability
        for (int i = 0; i < k; i++) {
            DecodeResult r = candidates.get(i);
            String text = r.asText();

            double rankScore = (k - i); // top gets highest
            double evScore = evidenceKeyScore(r) * evidenceWeight;

            textScore.put(text, textScore.getOrDefault(text, 0.0) + rankScore + evScore);
            textRuns.put(text, textRuns.getOrDefault(text, 0) + 1);
        }

        DecodeResult top = candidates.get(0);
        history.add(new VoteSnapshot(runIndex, top.asText(), evidenceKeys(top)));
    }

    private double evidenceKeyScore(DecodeResult r) {
        // simple: more distinct evidence keys -> higher (placeholder)
        return evidenceKeys(r).size() / 10.0;
    }

    private Set<String> evidenceKeys(DecodeResult r) {
        Set<String> s = new LinkedHashSet<>();
        for (EvidenceRecord rec : r.evidence().records()) {
            s.add(rec.key());
        }
        return s;
    }

    public List<VoteScore> topN(int n) {
        List<VoteScore> list = new ArrayList<>();
        for (Map.Entry<String, Double> e : textScore.entrySet()) {
            String text = e.getKey();
            list.add(new VoteScore(text, e.getValue(), textRuns.getOrDefault(text, 0)));
        }
        list.sort((a,b) -> Double.compare(b.score, a.score));
        if (list.size() > n) return list.subList(0, n);
        return list;
    }

    public List<VoteSnapshot> history() {
        return Collections.unmodifiableList(history);
    }
}
