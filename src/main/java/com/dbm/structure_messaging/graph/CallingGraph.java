package com.dbm.structure_messaging.graph;

import com.dbm.structure_messaging.model.WordToken;

import java.util.List;

/**
 * Minimal grammar constraint / calling graph.
 * Think of it as an automaton: state + token -> next state(s).
 */
public interface CallingGraph {
    int startState();
    boolean isAcceptState(int state);

    /** Return next states given (state, token). Empty means invalid transition. */
    List<Integer> nextStates(int state, WordToken token);
}
