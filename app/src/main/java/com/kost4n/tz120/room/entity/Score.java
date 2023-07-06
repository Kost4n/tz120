package com.kost4n.tz120.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
       tableName = "scores"
)
public class Score {
    @PrimaryKey(
            autoGenerate = false
    )
    public long id;
    public double score_satoshi;
    public double score_btc;

    public Score(int id, double score_satoshi, double score_btc) {
        this.id = id;
        this.score_satoshi = score_satoshi;
        this.score_btc = score_btc;
    }

    public Score() {
    }

    public double getScore_btc() {
        return score_btc;
    }

    public double getScore_satoshi() {
        return score_satoshi;
    }
}
