package com.example.a2048;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
parentColumns = "uid",
childColumns = "user")})
public class TwentyScore {

    public TwentyScore(){

    }

    @Ignore
    public TwentyScore(int score, long time, int user_id, int size, int maxScore){
        this.score = score;
        this.time = time;
        this.user = user_id;
        this.size = size;
        this.maxScore = maxScore;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "score")
    public int score;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "user")
    public int user;

    @ColumnInfo(name = "size")
    public int size;

    @ColumnInfo(name = "maxScore")
    public int maxScore;
}
