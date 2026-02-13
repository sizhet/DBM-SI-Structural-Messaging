package com.dbm.structure_messaging.trainer;

import com.dbm.structure_messaging.table.WordEncodingTable;

import java.util.List;

public interface IEncodingTableTrainer {
    WordEncodingTable train(List<TrainingSample> samples, TrainerConfig cfg);
}
