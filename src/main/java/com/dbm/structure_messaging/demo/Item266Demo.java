package com.dbm.structure_messaging.demo;

import com.dbm.structure_messaging.decode.*;
import com.dbm.structure_messaging.decode.DecodeResult;
import com.dbm.structure_messaging.decode.DecoderConfig;
import com.dbm.structure_messaging.decode.PermutationTreeDecoder;
import com.dbm.structure_messaging.graph.SimpleCallingGraph;
import com.dbm.structure_messaging.model.WordToken;
import com.dbm.structure_messaging.stream.*;
import com.dbm.structure_messaging.table.*;
import com.dbm.structure_messaging.stream.ArrayBitStream;
import com.dbm.structure_messaging.stream.BitStreamReader;
import com.dbm.structure_messaging.table.EncodingEntry;
import com.dbm.structure_messaging.table.InMemoryWordEncodingTable;
import com.dbm.structure_messaging.vote.VoteAggregator;

import java.util.List;

public final class Item266Demo {
    public static void main(String[] args) {
        // vocab
        WordToken 奥 = new WordToken("奥");
        WordToken 博 = new WordToken("博");
        WordToken 特 = new WordToken("特");
        WordToken 你 = new WordToken("你");
        WordToken 好 = new WordToken("好");

        // table: head(2bits packed) -> candidates + tail
        // packed: 00=0, 01=1, 10=2, 11=3
        InMemoryWordEncodingTable table = new InMemoryWordEncodingTable(2, 30)
                .put(0, new EncodingEntry(奥, 8), new EncodingEntry(你, 5))
                .put(1, new EncodingEntry(博, 13), new EncodingEntry(好, 3))
                .put(2, new EncodingEntry(特, 6))
                .put(3, new EncodingEntry(你, 8), new EncodingEntry(好, 6));

        // calling graph for "奥博特你好" (very small FSA)
        SimpleCallingGraph g = new SimpleCallingGraph(0)
                .edge(0, 奥, 1)
                .edge(1, 博, 2)
                .edge(2, 特, 3)
                .edge(3, 你, 4)
                .edge(4, 好, 5)
                .accept(5);

        DecoderConfig cfg = new DecoderConfig();
        cfg.maxTokens = 8;
        cfg.beamWidth = 128;
        cfg.maxNodesExpanded = 20_000;

        PermutationTreeDecoder decoder = new PermutationTreeDecoder(table, g, cfg);

        VoteAggregator vote = new VoteAggregator();

        // simulate multiple runs with different streams
        for (int run = 0; run < 5; run++) {
            int[] bits = toyBits(run);
            BitStreamReader reader = new BitStreamReader(new ArrayBitStream(bits));
            List<DecodeResult> candidates = decoder.decode(reader, 0);

            System.out.println("Run#" + run + " candidates:");
            for (int i = 0; i < Math.min(3, candidates.size()); i++) {
                System.out.println("  - " + candidates.get(i).asText());
            }
            vote.addRun(candidates);
        }

        System.out.println("Vote top:");
        for (VoteAggregator.VoteResult r : vote.topK(3)) System.out.println("  " + r);
    }

    private static int[] toyBits(int seed) {
        // Tiny deterministic stream generator (NOT crypto). Just for demo.
        int n = 200;
        int[] a = new int[n];
        long x = 1469598103934665603L ^ (seed * 1315423911L);
        for (int i = 0; i < n; i++) {
            x ^= (x << 13); x ^= (x >>> 7); x ^= (x << 17);
            a[i] = (int)(x & 1L);
        }
        return a;
    }
}
