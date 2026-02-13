package com.dbm.si.passive266.trainer;

import com.dbm.si.passive266.model.WordToken;
import com.dbm.si.passive266.table.EncodingEntry;
import com.dbm.si.passive266.table.InMemoryWordEncodingTable;
import com.dbm.si.passive266.table.WordEncodingTable;

import java.util.*;

/**
 * Offline trainer (skeleton):
 * - Assign each token a head bucket (0..2^H-1)
 * - Assign tail lengths
 * - Optimize objective:
 *    collision minimization + fanout minimization + tail distribution balance
 *
 * This is a starting point; you can replace with ML/ILP/GA later.
 */
public final class HeuristicEncodingTableTrainer implements IEncodingTableTrainer {

    @Override
    public WordEncodingTable train(List<TrainingSample> samples, TrainerConfig cfg) {
        if (samples == null || samples.isEmpty()) throw new IllegalArgumentException("samples");
        Random rnd = new Random(cfg.seed);

        // collect vocabulary
        LinkedHashSet<WordToken> vocab = new LinkedHashSet<>();
        for (TrainingSample s : samples) vocab.addAll(s.sentenceTokens);
        List<WordToken> tokens = new ArrayList<>(vocab);

        int buckets = 1 << cfg.headBitWidth;

        // initial assignment: random bucket, random tail
        int[] bucketOf = new int[tokens.size()];
        int[] tailOf = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            bucketOf[i] = rnd.nextInt(buckets);
            tailOf[i] = 1 + rnd.nextInt(Math.max(1, cfg.maxTailLength));
        }

        double best = objective(tokens, bucketOf, tailOf, cfg);
        int[] bestBucket = bucketOf.clone();
        int[] bestTail = tailOf.clone();

        // local search
        for (int it = 0; it < cfg.iterations; it++) {
            int idx = rnd.nextInt(tokens.size());
            int oldB = bucketOf[idx];
            int oldT = tailOf[idx];

            // mutate
            bucketOf[idx] = rnd.nextInt(buckets);
            tailOf[idx] = 1 + rnd.nextInt(Math.max(1, cfg.maxTailLength));

            double now = objective(tokens, bucketOf, tailOf, cfg);

            // accept if better (greedy skeleton)
            if (now < best) {
                best = now;
                bestBucket = bucketOf.clone();
                bestTail = tailOf.clone();
            } else {
                // revert
                bucketOf[idx] = oldB;
                tailOf[idx] = oldT;
            }
        }

        // build table
        InMemoryWordEncodingTable table = new InMemoryWordEncodingTable(cfg.headBitWidth, cfg.maxTailLength);
        Map<Integer, List<EncodingEntry>> tmp = new HashMap<>();
        for (int i = 0; i < tokens.size(); i++) {
            tmp.computeIfAbsent(bestBucket[i], k -> new ArrayList<>())
                    .add(new EncodingEntry(tokens.get(i), bestTail[i]));
        }
        for (Map.Entry<Integer, List<EncodingEntry>> e : tmp.entrySet()) {
            table.put(e.getKey(), e.getValue().toArray(new EncodingEntry[0]));
        }
        return table;
    }

    private double objective(List<WordToken> tokens, int[] bucketOf, int[] tailOf, TrainerConfig cfg) {
        // (1) collision proxy: same bucket = potential collision
        Map<Integer, Integer> bucketCounts = new HashMap<>();
        for (int b : bucketOf) bucketCounts.put(b, bucketCounts.getOrDefault(b, 0) + 1);

        double collision = 0.0;
        double fanout = 0.0;
        for (int c : bucketCounts.values()) {
            if (c > 1) collision += (c - 1);
            fanout += c * c; // penalize large bucket fanout
        }

        // (2) tail balance proxy: penalize high variance / extreme tails
        double mean = 0.0;
        for (int t : tailOf) mean += t;
        mean /= Math.max(1, tailOf.length);

        double var = 0.0;
        for (int t : tailOf) {
            double d = t - mean;
            var += d * d;
        }
        var /= Math.max(1, tailOf.length);

        return cfg.wCollision * collision
                + cfg.wHeadFanout * fanout
                + cfg.wTailBalance * var;
    }
}
