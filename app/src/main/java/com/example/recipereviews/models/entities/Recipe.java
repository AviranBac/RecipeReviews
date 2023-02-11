package com.example.recipereviews.models.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {

    @PrimaryKey
    @NonNull
    private int id;
    private String name;
    private String img;
    private Long lastUpdateTime;
    private int preparationTime;
    private List<String> ingredients;
    private List<String> instructions;

    @Ignore
    public Recipe() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
