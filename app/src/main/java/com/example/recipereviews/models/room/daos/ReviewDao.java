package com.example.recipereviews.models.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.ReviewWithUser;

import java.util.List;

@Dao
public interface ReviewDao {
    @Transaction
    @Query("select * from Review where recipeId = :recipeId")
    List<ReviewWithUser> getByRecipeId(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Review... reviews);
}
