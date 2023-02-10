package com.example.recipereviews.models.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.recipereviews.models.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("select * from Recipe where name LIKE :search")
    LiveData<List<Recipe>> getByName(String search);

    @Query("select * from Recipe where id = :id")
    Recipe getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);
}
