package com.dbm.si.passive266.trainer;

public final class TrainerConfig {
    public int headBitWidth = 2;
    public int maxTailLength = 30;

    // optimization weights (heuristic)
    public double wCollision = 5.0;
    public double wHeadFanout = 2.0;
    public double wTailBalance = 1.0;

    public int iterations = 2000;
    public long seed = 42L;
}
