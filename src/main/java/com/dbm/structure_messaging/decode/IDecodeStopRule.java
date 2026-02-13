package com.dbm.structure_messaging.decode;

public interface IDecodeStopRule {
    StopDecision check(StopContext ctx);
}
