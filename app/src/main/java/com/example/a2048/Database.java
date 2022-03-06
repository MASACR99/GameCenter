package com.example.a2048;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {User.class,PegScore.class,TwentyScore.class}, version = 3)
public abstract class Database extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PegScoreDao pegDao();
    public abstract TwentyScoreDao twentyDao();
}
