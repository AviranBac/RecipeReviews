package com.example.recipereviews.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReviewWithUser {
    @Embedded
    public Review review;
    @Relation(
            parentColumn = "userId",
            entityColumn = "id"
    )
    public User user;
}
