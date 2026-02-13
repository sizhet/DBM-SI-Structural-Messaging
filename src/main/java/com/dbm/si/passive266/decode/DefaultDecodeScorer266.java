package com.dbm.si.passive266.decode;

import com.dbm.si.passive266.model.WordToken;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple, deterministic scorer:
 * - token coverage gain (if desiredTokenSet provided)
 * - slight preference for moderate tail lengths (avoid extreme)
 * - evidence stability bonus (if tracker provided)
 *
 * NOTE: This does NOT claim correctness; it's a DBM-style heuristic hook.
 */
public final class DefaultDecodeScorer266 implements IDecodeScorer {

    @Override
    public double score(ScoreContext ctx) {
        double s = 0.0;

        // 1) token coverage gain
        if (ctx.desiredTokenSet != null && !ctx.desiredTokenSet.isEmpty()) {
            Set<WordToken> seen = new HashSet<>(ctx.pathBefore);
            boolean newToken = !seen.contains(ctx.token) && ctx.desiredTokenSet.contains(ctx.token);
            if (newToken) s += 2.0;
        }

        // 2) tail-length "balance" (avoid extremes)
        // prefer tail in [2..20] mildly
        if (ctx.tailLength >= 2 && ctx.tailLength <= 20) s += 0.3;
        else s -= 0.2;

        // 3) stability bonus (if provided)
        if (ctx.stableKeyTracker != null) {
            s += 1.0 * ctx.stableKeyTracker.stabilityScore();
        }

        // 4) small step bonus (encourage progress)
        long step = ctx.posAfter - ctx.posBefore;
        if (step > 0) s += Math.min(0.5, step / 1000.0);

        return s;
    }
}
