package com.dbm.si.passive266.graph;

import com.dbm.si.passive266.model.WordToken;

import java.util.*;

/**
 * Very small in-memory FSA:
 * transitions: state -> (token -> nextState)
 */
public final class SimpleCallingGraph implements CallingGraph {
    private final int startState;
    private final Set<Integer> accept = new HashSet<>();
    private final Map<Integer, Map<WordToken, Integer>> edges = new HashMap<>();

    public SimpleCallingGraph(int startState) {
        this.startState = startState;
    }

    public SimpleCallingGraph accept(int state) { accept.add(state); return this; }

    public SimpleCallingGraph edge(int from, WordToken token, int to) {
        edges.computeIfAbsent(from, k -> new HashMap<>()).put(token, to);
        return this;
    }

    @Override public int startState() { return startState; }

    @Override public boolean isAcceptState(int state) { return accept.contains(state); }

    @Override
    public List<Integer> nextStates(int state, WordToken token) {
        Map<WordToken, Integer> m = edges.get(state);
        if (m == null) return Collections.emptyList();
        Integer to = m.get(token);
        return to == null ? Collections.emptyList() : Collections.singletonList(to);
    }
}
