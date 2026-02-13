package com.dbm.si.passive266.trainer;

import com.dbm.si.passive266.table.WordEncodingTable;

import java.util.List;

public interface IEncodingTableTrainer {
    WordEncodingTable train(List<TrainingSample> samples, TrainerConfig cfg);
}
