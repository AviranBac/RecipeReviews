package com.example.recipereviews.models.room.daos;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.recipereviews.models.entities.User;

public interface UserDao {

    @Query("select * from User where id = :id")
    User getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);
}
