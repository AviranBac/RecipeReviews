package com.example.recipereviews.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReviewWithRecipe {
    @Embedded
    private Review review;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "id"
    )
    private Recipe recipe;

    public Review getReview() {
        return this.review;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
