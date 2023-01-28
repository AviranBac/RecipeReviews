package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import java.util.List;

public class GetRecipesDto {
    private List<Recipe> results;

    public List<Recipe> getResults() {
        return this.results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }
}
