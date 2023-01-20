package com.example.recipereviews.models.room;

import androidx.room.Room;

import com.example.recipereviews.ApplicationContext;

public class RecipeReviewsLocalDb {

    public static RecipeReviewsLocalDbRepository getLocalDb() {
        return Room.databaseBuilder(
                ApplicationContext.getContext(),
                RecipeReviewsLocalDbRepository.class,
                "recipeReviewsDb.db"
        )
                .fallbackToDestructiveMigration()
                .build();
    }
}
