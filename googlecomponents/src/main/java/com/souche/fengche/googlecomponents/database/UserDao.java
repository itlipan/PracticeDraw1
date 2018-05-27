package com.souche.fengche.googlecomponents.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAllUser();

    @Query("SELECT * FROM User WHERE id=:id")
    User getUserById(int id);

    @Query("SELECT * FROM User")
    Cursor getUserCursor();

    @Query("SELECT * FROM User WHERE name =:name LIMIT :max")
    List<User> getLimitUsersByName(int max, String name);

    @Insert
    void insertUser(User... users);

    @Insert
    void insertUserArray(List<User> userList);

    @Update
    void updateUser(User... users);

    @Delete
    void deleteUser(User... users);

}
