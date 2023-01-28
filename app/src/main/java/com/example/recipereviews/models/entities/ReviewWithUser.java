package com.example.recipereviews.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReviewWithUser {
    @Embedded
    private Review review;
    @Relation(
            parentColumn = "userId",
            entityColumn = "id"
    )
    private User user;

    public Review getReview() {
        return this.review;
    }

    public User getUser() {
        return this.user;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
