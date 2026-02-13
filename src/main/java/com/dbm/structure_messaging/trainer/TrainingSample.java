package com.dbm.structure_messaging.trainer;

import com.dbm.structure_messaging.model.WordToken;

import java.util.List;

public final class TrainingSample {
    public final List<WordToken> sentenceTokens;
    public TrainingSample(List<WordToken> sentenceTokens) {
        this.sentenceTokens = sentenceTokens;
    }
}
