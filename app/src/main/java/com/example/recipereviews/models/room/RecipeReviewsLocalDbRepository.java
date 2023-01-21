package com.example.recipereviews.models.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.room.daos.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class RecipeReviewsLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
}
