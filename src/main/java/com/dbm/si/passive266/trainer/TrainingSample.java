package com.dbm.si.passive266.trainer;

import com.dbm.si.passive266.model.WordToken;

import java.util.List;

public final class TrainingSample {
    public final List<WordToken> sentenceTokens;
    public TrainingSample(List<WordToken> sentenceTokens) {
        this.sentenceTokens = sentenceTokens;
    }
}
