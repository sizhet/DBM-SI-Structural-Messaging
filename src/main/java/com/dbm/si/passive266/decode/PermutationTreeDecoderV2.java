package com.dbm.si.passive266.decode;

import com.dbm.si.passive266.evidence.EvidenceChain;
import com.dbm.si.passive266.evidence.EvidenceRecord;
import com.dbm.si.passive266.graph.CallingGraph;
import com.dbm.si.passive266.model.HeadCode;
import com.dbm.si.passive266.model.WordToken;
import com.dbm.si.passive266.stream.BitStreamReader;
import com.dbm.si.passive266.table.EncodingEntry;
import com.dbm.si.passive266.table.WordEncodingTable;

import java.util.*;

/**
 * Decoder V2:
 * - integrates IDecodeScorer (beam ranking)
 * - exposes stop-rule hooks through IDecodeStopRule
 */
public final class PermutationTreeDecoderV2 {
    private final WordEncodingTable table;
    private final CallingGraph callingGraph;
    private final DecoderConfig cfg;
    private final IDecodeScorer scorer;
    private final IDecodeStopRule stopRule;

    public PermutationTreeDecoderV2(
            WordEncodingTable table,
            CallingGraph callingGraph,
            DecoderConfig cfg,
            IDecodeScorer scorer,
            IDecodeStopRule stopRule
    ) {
        if (table == null) throw new IllegalArgumentException("table");
        if (callingGraph == null) throw new IllegalArgumentException("callingGraph");
        this.table = table;
        this.callingGraph = callingGraph;
        this.cfg = cfg == null ? new DecoderConfig() : cfg;
        this.scorer = scorer == null ? new DefaultDecodeScorer266() : scorer;
        this.stopRule = stopRule == null ? new MaxNodesMaxDepthStopRule(cfg.maxNodesExpanded, cfg.maxTokens) : stopRule;
    }

    public DecodeRunResult decode(BitStreamReader reader, long startPos, DecodeHints hints) {
        int H = table.headBitWidth();

        long expanded = 0;
        List<DecodeNode> frontier = new ArrayList<>();
        frontier.add(new DecodeNode(
                startPos,
                callingGraph.startState(),
                Collections.emptyList(),
                new EvidenceChain().add("startPos", String.valueOf(startPos))
        ));

        List<DecodeResult> accepted = new ArrayList<>();
        StableKeyTracker tracker = hints == null ? null : hints.stableKeyTracker;

        for (int depth = 0; depth < cfg.maxTokens; depth++) {
            if (frontier.isEmpty()) break;

            // stop-rule (pre-layer)
            StopDecision pre = stopRule.check(new StopContext(depth, expanded, accepted, frontier));
            if (pre.shouldStop) {
                return new DecodeRunResult(dedup(accepted), pre.reason, expanded, depth);
            }

            List<BeamCandidate> candidates = new ArrayList<>();

            for (DecodeNode node : frontier) {
                if (expanded++ > cfg.maxNodesExpanded) break;

                HeadCode head = reader.readHead(node.pos(), H);
                List<EncodingEntry> entries = table.lookup(head);
                if (entries.isEmpty()) continue;

                for (EncodingEntry e : entries) {
                    WordToken token = e.token();
                    List<Integer> ns = callingGraph.nextStates(node.state(), token);
                    if (ns.isEmpty()) continue;

                    int tail = e.tailLength();
                    if (tail > table.maxTailLength()) continue;

                    long nextPos = node.pos() + H + tail;

                    for (int nextState : ns) {
                        EvidenceChain ev = new EvidenceChain();
                        for (EvidenceRecord rec : node.evidence().records()) ev.add(rec.key(), rec.value());
                        ev.add("head", head.toBitString());
                        ev.add("token", token.text());
                        ev.add("tail", String.valueOf(tail));
                        ev.add("pos", node.pos() + "->" + nextPos);

                        if (tracker != null) {
                            tracker.observeKey("token:" + token.text());
                            tracker.observeKey("head:" + head.toBitString());
                        }

                        DecodeNode child = node.extend(nextPos, nextState, token, ev);

                        ScoreContext sctx = new ScoreContext(
                                head, token, tail, node.pos(), nextPos,
                                node.state(), nextState,
                                node.path(),
                                hints == null ? null : hints.desiredTokenSet,
                                tracker
                        );
                        double sc = scorer.score(sctx);
                        candidates.add(new BeamCandidate(child, sc));
                    }
                }
            }

            // sort by score desc
            candidates.sort((a,b) -> Double.compare(b.score, a.score));

            // beam cap
            List<DecodeNode> next = new ArrayList<>();
            int cap = Math.min(cfg.beamWidth, candidates.size());
            for (int i = 0; i < cap; i++) next.add(candidates.get(i).node);

            // collect accept states
            for (DecodeNode n : next) {
                if (callingGraph.isAcceptState(n.state())) {
                    accepted.add(new DecodeResult(n.path(), n.evidence()));
                }
            }

            frontier = next;

            // stop-rule (post-layer)
            StopDecision post = stopRule.check(new StopContext(depth, expanded, accepted, frontier));
            if (post.shouldStop) {
                return new DecodeRunResult(dedup(accepted), post.reason, expanded, depth);
            }
        }

        return new DecodeRunResult(dedup(accepted), "MAX_DEPTH", expanded, cfg.maxTokens);
    }

    private static List<DecodeResult> dedup(List<DecodeResult> accepted) {
        Map<String, DecodeResult> m = new LinkedHashMap<>();
        for (DecodeResult r : accepted) m.putIfAbsent(r.asText(), r);
        return new ArrayList<>(m.values());
    }

    public static final class DecodeHints {
        public final Set<WordToken> desiredTokenSet;
        public final StableKeyTracker stableKeyTracker;

        public DecodeHints(Set<WordToken> desiredTokenSet, StableKeyTracker stableKeyTracker) {
            this.desiredTokenSet = desiredTokenSet;
            this.stableKeyTracker = stableKeyTracker;
        }
    }

    public static final class DecodeRunResult {
        public final List<DecodeResult> acceptedCandidates;
        public final String stopReason;
        public final long nodesExpanded;
        public final int depthReached;

        public DecodeRunResult(List<DecodeResult> acceptedCandidates, String stopReason, long nodesExpanded, int depthReached) {
            this.acceptedCandidates = acceptedCandidates;
            this.stopReason = stopReason;
            this.nodesExpanded = nodesExpanded;
            this.depthReached = depthReached;
        }

        public String topTextOrNull() {
            return acceptedCandidates.isEmpty() ? null : acceptedCandidates.get(0).asText();
        }
    }
}
