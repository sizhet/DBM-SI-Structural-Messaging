package com.dbm.structure_messaging;

import com.dbm.structure_messaging.decode.*;
import com.dbm.structure_messaging.graph.SimpleCallingGraph;
import com.dbm.structure_messaging.model.WordToken;
import com.dbm.structure_messaging.stream.*;
import com.dbm.structure_messaging.table.*;
import com.dbm.structure_messaging.decode.DecodeResult;
import com.dbm.structure_messaging.decode.DecoderConfig;
import com.dbm.structure_messaging.decode.PermutationTreeDecoder;
import com.dbm.structure_messaging.stream.ArrayBitStream;
import com.dbm.structure_messaging.stream.BitStreamReader;
import com.dbm.structure_messaging.table.EncodingEntry;
import com.dbm.structure_messaging.table.InMemoryWordEncodingTable;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PermutationTreeDecoderTest {

    @Test
    public void testDeterminism_sameStreamSameResult() {
        WordToken A = new WordToken("奥");
        WordToken B = new WordToken("博");
        WordToken C = new WordToken("特");

        InMemoryWordEncodingTable table = new InMemoryWordEncodingTable(2, 30)
                .put(0, new EncodingEntry(A, 2))
                .put(1, new EncodingEntry(B, 2))
                .put(2, new EncodingEntry(C, 2))
                .put(3, new EncodingEntry(A, 2));

        SimpleCallingGraph g = new SimpleCallingGraph(0)
                .edge(0, A, 1)
                .edge(1, B, 2)
                .edge(2, C, 3)
                .accept(3);

        DecoderConfig cfg = new DecoderConfig();
        cfg.maxTokens = 3;
        cfg.beamWidth = 64;

        PermutationTreeDecoder dec = new PermutationTreeDecoder(table, g, cfg);

        int[] bits = new int[64];
        // craft a simple pattern
        for (int i = 0; i < bits.length; i++) bits[i] = (i % 2);

        BitStreamReader r1 = new BitStreamReader(new ArrayBitStream(bits));
        BitStreamReader r2 = new BitStreamReader(new ArrayBitStream(bits));

        List<DecodeResult> c1 = dec.decode(r1, 0);
        List<DecodeResult> c2 = dec.decode(r2, 0);

        Assert.assertEquals(c1.isEmpty(), c2.isEmpty());
        if (!c1.isEmpty()) {
            Assert.assertEquals(c1.get(0).asText(), c2.get(0).asText());
        }
    }

    @Test
    public void testCallingGraphPrunesInvalidTransitions() {
        WordToken X = new WordToken("X");
        WordToken Y = new WordToken("Y");

        InMemoryWordEncodingTable table = new InMemoryWordEncodingTable(2, 10)
                .put(0, new EncodingEntry(X, 1))
                .put(1, new EncodingEntry(Y, 1))
                .put(2, new EncodingEntry(X, 1))
                .put(3, new EncodingEntry(Y, 1));

        // Only allow X then Y, accept after 2 tokens
        SimpleCallingGraph g = new SimpleCallingGraph(0)
                .edge(0, X, 1)
                .edge(1, Y, 2)
                .accept(2);

        DecoderConfig cfg = new DecoderConfig();
        cfg.maxTokens = 4;

        PermutationTreeDecoder dec = new PermutationTreeDecoder(table, g, cfg);

        int[] bits = new int[128];
        for (int i = 0; i < bits.length; i++) bits[i] = 0;

        List<DecodeResult> out = dec.decode(new BitStreamReader(new ArrayBitStream(bits)), 0);
        // Acceptable candidates must match XY
        for (DecodeResult r : out) {
            Assert.assertEquals("XY", r.asText());
        }
    }
}
