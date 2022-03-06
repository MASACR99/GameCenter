package com.example.a2048;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PegScoreDao {
    @Query("SELECT * FROM pegscore")
    List<PegScore> getAll();

    @Query("SELECT * FROM pegscore WHERE user = (:userId)")
    List<PegScore> loadByUserId(int userId);

    @Query("SELECT * FROM pegscore WHERE score > :minScore ORDER BY time ASC")
    List<PegScore> loadScoreHigherThan(int minScore);

    @Query("SELECT * FROM pegscore WHERE time < :maxTime ORDER BY time ASC")
    List<PegScore> loadTimeLessThan(int maxTime);

    @Query("SELECT * FROM (SELECT * FROM pegscore ORDER BY time ASC) LIMIT :limit")
    List<PegScore> returnTopScores(int limit);

    @Query("SELECT * FROM pegscore WHERE user = :userid")
    List<PegScore> returnUserScores(int userid);

    @Query("SELECT * FROM (SELECT * FROM pegscore WHERE modality LIKE :modality ORDER BY time ASC) LIMIT :limit")
    List<PegScore> returnTopByModality(int limit, String modality);

    @Insert
    void insertAll(PegScore... twenty);

    @Delete
    void delete(PegScore twenty);
}
