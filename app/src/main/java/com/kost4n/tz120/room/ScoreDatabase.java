package com.kost4n.tz120.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kost4n.tz120.room.dao.ScoreDao;
import com.kost4n.tz120.room.entity.Score;

@Database(
        entities = {
                Score.class
        },
        version = 1
)
public abstract class ScoreDatabase  extends RoomDatabase {
    public abstract ScoreDao getScoreDao();
    private static ScoreDatabase instance;
    private static final String DB_NAME = "database.db";


    public static ScoreDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (ScoreDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        ScoreDatabase.class, DB_NAME).allowMainThreadQueries().build();
            }
        }
        return instance;
    }
}

