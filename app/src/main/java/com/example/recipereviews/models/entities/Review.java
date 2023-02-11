package com.example.recipereviews.models.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.recipereviews.utils.ApplicationContext;
import com.example.recipereviews.utils.FirebaseUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Review {
    private static final String LOCAL_LAST_UPDATE_TIME = "reviewLocalLastUpdateTime";
    private static final String LAST_UPDATE_TIME_FIELD = "lastUpdateTime";

    @PrimaryKey
    @NonNull
    private String id;
    private int recipeId;
    private String userId;
    private double rating;
    private String imageUrl;
    private String description;
    private Long lastUpdateTime;
    private boolean isDeleted;

    public Review(@NonNull String id, int recipeId, String userId, double rating, String imageUrl, String description, Long lastUpdateTime, boolean isDeleted) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.description = description;
        this.lastUpdateTime = lastUpdateTime;
        this.isDeleted = isDeleted;
    }

    @Ignore
    public Review(int recipeId, String userId, double rating, String description) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.rating = rating;
        this.description = description;
        this.isDeleted = false;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("recipeId", recipeId);
            put("userId", userId);
            put("rating", rating);
            put("imageUrl", imageUrl);
            put("description", description);
            put("lastUpdateTime", FieldValue.serverTimestamp());
            put("isDeleted", isDeleted);
        }};
    }

    public static Review create(Map<String, Object> review, String id) {
        String imageUrl = review.get("imageUrl") != null ? review.get("imageUrl").toString() : null;
        long lastUpdateTime = ((Timestamp) Objects.requireNonNull(review.get("lastUpdateTime"))).getSeconds();

        return new Review(
                id,
                ((Long) Objects.requireNonNull(review.get("recipeId"))).intValue(),
                Objects.requireNonNull(review.get("userId")).toString(),
                FirebaseUtils.parseDouble(Objects.requireNonNull(review.get("rating"))),
                imageUrl,
                Objects.requireNonNull(review.get("description")).toString(),
                lastUpdateTime,
                (boolean) Objects.requireNonNull(review.get("isDeleted"))
        );
    }

    public static String getLastUpdateTimeField() {
        return LAST_UPDATE_TIME_FIELD;
    }

    public static Long getLocalLastUpdateTime() {
        return ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(Review.LOCAL_LAST_UPDATE_TIME, 0);
    }

    public static void setLocalLastUpdateTime(Long localLastUpdateTime) {
        ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .edit()
                .putLong(Review.LOCAL_LAST_UPDATE_TIME, localLastUpdateTime)
                .apply();
    }
}
