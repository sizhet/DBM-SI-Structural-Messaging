package com.dbm.structure_messaging.vote;

import com.dbm.structure_messaging.decode.DecodeResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VoteAggregator {
    public static final class VoteResult {
        public final String text;
        public final int votes;
        public VoteResult(String text, int votes) { this.text = text; this.votes = votes; }
        @Override public String toString() { return text + " (votes=" + votes + ")"; }
    }

    private final Map<String, Integer> counts = new HashMap<>();

    public void addRun(List<DecodeResult> candidates) {
        // simplest: vote only top-1; you can also vote top-k
        if (candidates == null || candidates.isEmpty()) return;
        String top = candidates.get(0).asText();
        counts.put(top, counts.getOrDefault(top, 0) + 1);
    }

    public List<VoteResult> topK(int k) {
        List<VoteResult> list = new ArrayList<>();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            list.add(new VoteResult(e.getKey(), e.getValue()));
        }
        list.sort((a,b) -> Integer.compare(b.votes, a.votes));
        if (list.size() > k) return list.subList(0, k);
        return list;
    }
}
