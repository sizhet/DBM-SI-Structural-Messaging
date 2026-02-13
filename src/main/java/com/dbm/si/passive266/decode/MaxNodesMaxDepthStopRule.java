package com.dbm.si.passive266.decode;

public final class MaxNodesMaxDepthStopRule implements IDecodeStopRule {
    private final long maxNodes;
    private final int maxDepth;

    public MaxNodesMaxDepthStopRule(long maxNodes, int maxDepth) {
        this.maxNodes = maxNodes;
        this.maxDepth = maxDepth;
    }

    @Override
    public StopDecision check(StopContext ctx) {
        if (ctx.nodesExpanded >= maxNodes) return StopDecision.stop("MAX_NODES");
        if (ctx.depth >= maxDepth) return StopDecision.stop("MAX_DEPTH");
        return StopDecision.go();
    }
}
