package com.example.recipereviews.models.entities;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.recipereviews.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    private static final String LOCAL_LAST_UPDATE_TIME = "recipeLocalLastUpdateTime";
    @PrimaryKey
    private int id;
    private String name;
    private String img;
    private Long lastUpdateTime;
    private int preparationTime;
    private List<String> ingredients;
    private List<String> instructions;

    @Ignore
    public Recipe(int id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.preparationTime = 0;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public Recipe(int id, String name, String img, int preparationTime, List<String> ingredients, List<String> instructions) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getImg() {
        return this.img;
    }

    public Long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getPreparationTime() {
        return this.preparationTime;
    }

    public List<String> getIngredients() {
        return this.ingredients;
    }


    public List<String> getInstructions() {
        return this.instructions;
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
