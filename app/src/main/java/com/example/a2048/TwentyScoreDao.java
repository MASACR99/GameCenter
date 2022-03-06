package com.example.a2048;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TwentyScoreDao {
    @Query("SELECT * FROM twentyscore")
    List<TwentyScore> getAll();

    @Query("SELECT * FROM twentyscore WHERE user = (:userId)")
    List<TwentyScore> loadByUserId(int userId);

    @Query("SELECT * FROM twentyscore WHERE score > :minScore ORDER BY score DESC")
    List<TwentyScore> loadScoreHigherThan(int minScore);

    @Query("SELECT * FROM twentyscore WHERE time < :maxTime ORDER BY time DESC")
    List<TwentyScore> loadTimeLessThan(int maxTime);

    @Query("SELECT * FROM (SELECT * FROM twentyscore ORDER BY score DESC) LIMIT :limit")
    List<TwentyScore> returnTopScores(int limit);

    @Query("SELECT * FROM (SELECT * FROM twentyscore ORDER BY time ASC) LIMIT :limit")
    List<TwentyScore> returnTopTimes(int limit);

    @Query("SELECT * FROM twentyscore WHERE user = :userid")
    List<TwentyScore> returnUserScores(int userid);

    @Query("SELECT * FROM (SELECT * FROM twentyscore WHERE size = :size ORDER BY score ASC) LIMIT :limit")
    List<TwentyScore> returnTopBySize(int limit, int size);

    @Insert
    void insertAll(TwentyScore... twenty);

    @Delete
    void delete(TwentyScore twenty);

}
