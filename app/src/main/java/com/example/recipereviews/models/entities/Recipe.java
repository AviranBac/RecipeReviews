package com.example.recipereviews.models.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recipereviews.ApplicationContext;

@Entity
public class Recipe {
    private static final String LOCAL_LAST_UPDATE_TIME = "recipeLocalLastUpdateTime";

    @PrimaryKey
    private int id;
    private String name;
    private String img;
    private Long lastUpdateTime;

    public Recipe(int id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }


    public static Long getLocalLastUpdateTime() {
        return ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(Recipe.LOCAL_LAST_UPDATE_TIME, 0);
    }

    public static void setLocalLastUpdateTime(Long localLastUpdateTime) {
        ApplicationContext.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .edit()
                .putLong(Recipe.LOCAL_LAST_UPDATE_TIME, localLastUpdateTime)
                .apply();
    }
}
