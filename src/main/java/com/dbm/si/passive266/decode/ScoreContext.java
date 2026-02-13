package com.dbm.si.passive266.decode;

import com.dbm.si.passive266.model.HeadCode;
import com.dbm.si.passive266.model.WordToken;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class ScoreContext {
    public final HeadCode head;
    public final WordToken token;
    public final int tailLength;
    public final long posBefore;
    public final long posAfter;

    public final int callingGraphStateBefore;
    public final int callingGraphStateAfter;

    /** current path before applying this step */
    public final List<WordToken> pathBefore;

    /** target token-set constraint (optional) */
    public final Set<WordToken> desiredTokenSet;

    /** for "evidence stability" heuristics (optional) */
    public final StableKeyTracker stableKeyTracker;

    public ScoreContext(
            HeadCode head,
            WordToken token,
            int tailLength,
            long posBefore,
            long posAfter,
            int callingGraphStateBefore,
            int callingGraphStateAfter,
            List<WordToken> pathBefore,
            Set<WordToken> desiredTokenSet,
            StableKeyTracker stableKeyTracker
    ) {
        this.head = head;
        this.token = token;
        this.tailLength = tailLength;
        this.posBefore = posBefore;
        this.posAfter = posAfter;
        this.callingGraphStateBefore = callingGraphStateBefore;
        this.callingGraphStateAfter = callingGraphStateAfter;
        this.pathBefore = pathBefore == null ? Collections.emptyList() : pathBefore;
        this.desiredTokenSet = desiredTokenSet;
        this.stableKeyTracker = stableKeyTracker;
    }
}
