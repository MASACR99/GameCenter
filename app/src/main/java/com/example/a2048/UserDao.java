package com.example.a2048;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE :username")
    User findByName(String username);

    @Query("SELECT * FROM user WHERE uid = :user_id")
    User findById(int user_id);

    @Query("UPDATE user SET username = :username WHERE uid = :user_id")
    void updateName(int user_id,String username);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
