package com.kost4n.tz120.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kost4n.tz120.room.entity.Score;

@Dao
public interface ScoreDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    void addScore(Score score);

    @Query("SELECT * FROM scores WHERE id = :id")
    Score getScoreById(long id);

    @Update
    void updateScore(Score score);
}
