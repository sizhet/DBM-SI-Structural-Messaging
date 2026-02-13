package com.dbm.structure_messaging.decode;

public final class StopDecision {
    public final boolean shouldStop;
    public final String reason;

    private StopDecision(boolean shouldStop, String reason) {
        this.shouldStop = shouldStop;
        this.reason = reason;
    }

    public static StopDecision stop(String reason) { return new StopDecision(true, reason); }
    public static StopDecision go() { return new StopDecision(false, ""); }
}
