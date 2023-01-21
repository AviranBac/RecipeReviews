package com.example.recipereviews.models.entities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.recipereviews.ApplicationContext;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    private static final String LOCAL_LAST_UPDATE_TIME = "userLocalLastUpdateTime";
    private static final String LAST_UPDATE_TIME_FIELD = "lastUpdateTime";

    @PrimaryKey
    @NonNull
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Long lastUpdateTime;

    @Ignore
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(@NonNull String id, String firstName, String lastName, String email, String imageUrl, Long lastUpdateTime) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.lastUpdateTime = lastUpdateTime;
    }

    public static User create(Map<String, Object> user, String id) {
        String imageUrl = user.get("imageUrl") != null ? user.get("imageUrl").toString() : null;
        long lastUpdateTime = ((Timestamp) user.get("lastUpdateTime")).getSeconds();

        return new User(
            id,
            user.get("firstName").toString(),
            user.get("lastName").toString(),
            user.get("email").toString(),
            imageUrl,
            lastUpdateTime
        );
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("firstName", firstName);
            put("lastName", lastName);
            put("email", email);
            put("imageUrl", imageUrl);
            put("lastUpdateTime", FieldValue.serverTimestamp());
        }};
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public static Long getLocalLastUpdateTime() {
        return ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(User.LOCAL_LAST_UPDATE_TIME, 0);
    }

    public static void setLocalLastUpdateTime(Long localLastUpdateTime) {
        ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .edit()
                .putLong(User.LOCAL_LAST_UPDATE_TIME, localLastUpdateTime)
                .apply();
    }
}
