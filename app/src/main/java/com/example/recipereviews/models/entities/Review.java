package com.example.recipereviews.models.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recipereviews.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Review {
    @PrimaryKey
    @NonNull
    private String id;
    private int recipeId;
    private String userId;
    private double rating;
    private String imageUrl;
    private String description;
    private Long lastUpdateTime;

    public Review(@NonNull String id, int recipeId, String userId, double rating, String imageUrl, String description, Long lastUpdateTime) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.description = description;
        this.lastUpdateTime = lastUpdateTime;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return this.userId;
    }

    public double getRating() {
        return this.rating;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("recipeId", recipeId);
            put("userId", userId);
            put("rating", rating);
            put("imageUrl", imageUrl);
            put("description", description);
            put("lastUpdateTime", FieldValue.serverTimestamp());
        }};
    }

    public static Review create(Map<String, Object> review, String id) {
        String imageUrl = review.get("imageUrl") != null ? review.get("imageUrl").toString() : null;
        long lastUpdateTime = ((Timestamp) Objects.requireNonNull(review.get("lastUpdateTime"))).getSeconds();

        return new Review(
                id,
                ((Long) Objects.requireNonNull(review.get("recipeId"))).intValue(),
                Objects.requireNonNull(review.get("userId")).toString(),
                FirebaseUtil.parseDouble(Objects.requireNonNull(review.get("rating"))),
                imageUrl,
                Objects.requireNonNull(review.get("description")).toString(),
                lastUpdateTime
        );
    }
}
