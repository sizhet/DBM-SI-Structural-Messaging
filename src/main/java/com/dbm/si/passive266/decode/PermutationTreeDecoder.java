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
import java.util.stream.Collectors;

/**
 * ITEM #266 core decoder:
 * - read head bits
 * - lookup candidates
 * - jump by tail-length
 * - prune by calling graph
 */
public final class PermutationTreeDecoder {
    private final WordEncodingTable table;
    private final CallingGraph callingGraph;
    private final DecoderConfig cfg;

    public PermutationTreeDecoder(WordEncodingTable table, CallingGraph callingGraph, DecoderConfig cfg) {
        if (table == null) throw new IllegalArgumentException("table");
        if (callingGraph == null) throw new IllegalArgumentException("callingGraph");
        this.table = table;
        this.callingGraph = callingGraph;
        this.cfg = cfg == null ? new DecoderConfig() : cfg;
    }

    public List<DecodeResult> decode(BitStreamReader reader, long startPos) {
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

        for (int depth = 0; depth < cfg.maxTokens; depth++) {
            if (frontier.isEmpty()) break;

            List<DecodeNode> next = new ArrayList<>();

            for (DecodeNode node : frontier) {
                if (expanded++ > cfg.maxNodesExpanded) break;

                HeadCode head = reader.readHead(node.pos(), H);
                List<EncodingEntry> entries = table.lookup(head);
                if (entries.isEmpty()) continue;

                for (EncodingEntry e : entries) {
                    WordToken token = e.token();

                    // calling graph prune
                    List<Integer> ns = callingGraph.nextStates(node.state(), token);
                    if (ns.isEmpty()) continue;

                    int tail = e.tailLength();
                    if (tail > table.maxTailLength()) continue;

                    long nextPos = node.pos() + H + tail;
                    for (int nextState : ns) {
                        EvidenceChain ev = new EvidenceChain();
                        ev.records().addAll(node.evidence().records()); // keep stable ordering
                        // (records() is unmodifiable, so copy explicitly)
                    }
                }
            }

            // The evidence copy above needs mutable access; keep simple: rebuild.
            next = expandLayer(reader, frontier, H);

            // beam cap: keep shortest paths (or you can add score later)
            if (next.size() > cfg.beamWidth) {
                next = next.subList(0, cfg.beamWidth);
            }

            // collect accept states
            for (DecodeNode n : next) {
                if (callingGraph.isAcceptState(n.state())) {
                    accepted.add(new DecodeResult(n.path(), n.evidence()));
                }
            }

            frontier = next;
        }

        // Dedup by text
        Map<String, DecodeResult> dedup = new LinkedHashMap<>();
        for (DecodeResult r : accepted) dedup.putIfAbsent(r.asText(), r);
        return new ArrayList<>(dedup.values());
    }

    private List<DecodeNode> expandLayer(BitStreamReader reader, List<DecodeNode> frontier, int H) {
        List<DecodeNode> next = new ArrayList<>();
        for (DecodeNode node : frontier) {
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
                    // copy old evidence
                    for (EvidenceRecord rec : node.evidence().records()) {
                        ev.add(rec.key(), rec.value());
                    }
                    ev.add("head", head.toBitString());
                    ev.add("token", token.text());
                    ev.add("tail", String.valueOf(tail));
                    ev.add("pos", node.pos() + "->" + nextPos);

                    next.add(node.extend(nextPos, nextState, token, ev));
                }
            }
        }

        // stable ordering by path length then lex
        next.sort(Comparator
                .comparingInt((DecodeNode n) -> n.path().size())
                .thenComparing(n -> n.path().stream().map(WordToken::text).collect(Collectors.joining(""))));
        return next;
    }
}
