package com.dbm.si.passive266;

import com.dbm.si.passive266.model.HeadCode;
import com.dbm.si.passive266.model.WordToken;
import com.dbm.si.passive266.table.WordEncodingTable;
import com.dbm.si.passive266.trainer.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TrainerBuildsTableTest {

    @Test
    public void testTrainerProducesNonEmptyTable() {
        TrainingSample s1 = new TrainingSample(Arrays.asList(new WordToken("奥"), new WordToken("博"), new WordToken("特")));
        TrainingSample s2 = new TrainingSample(Arrays.asList(new WordToken("你"), new WordToken("好")));

        TrainerConfig cfg = new TrainerConfig();
        cfg.headBitWidth = 2;
        cfg.maxTailLength = 20;
        cfg.iterations = 200;

        IEncodingTableTrainer trainer = new HeuristicEncodingTableTrainer();
        WordEncodingTable table = trainer.train(Arrays.asList(s1, s2), cfg);

        Assert.assertEquals(2, table.headBitWidth());
        // At least one head bucket should have entries
        boolean any = false;
        for (int b = 0; b < (1 << table.headBitWidth()); b++) {
            if (!table.lookup(new HeadCode(b, table.headBitWidth())).isEmpty()) {
                any = true; break;
            }
        }
        Assert.assertTrue(any);
    }
}
