package com.example.recipereviews.models.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.room.daos.RecipeDao;
import com.example.recipereviews.models.room.daos.ReviewDao;
import com.example.recipereviews.models.room.daos.UserDao;
import com.example.recipereviews.utils.TypeConverters;

@Database(entities = {User.class, Recipe.class, Review.class}, version = 15)
@androidx.room.TypeConverters({TypeConverters.class})
public abstract class RecipeReviewsLocalDbRepository extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract RecipeDao recipeDao();

    public abstract ReviewDao reviewDao();
}
