package com.dbm.structure_messaging.decode;

import java.util.List;

public final class StopContext {
    public final int depth;
    public final long nodesExpanded;
    public final List<DecodeResult> accepted;
    public final List<DecodeNode> frontier;

    public StopContext(int depth, long nodesExpanded, List<DecodeResult> accepted, List<DecodeNode> frontier) {
        this.depth = depth;
        this.nodesExpanded = nodesExpanded;
        this.accepted = accepted;
        this.frontier = frontier;
    }
}
