package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeTypeAdapter extends TypeAdapter<Recipe> {

    @Override
    public void write(JsonWriter out, Recipe value) {

    }

    @Override
    public Recipe read(JsonReader in) throws IOException {
        JsonObject jsonRecipe = new Gson().getAdapter(JsonObject.class).read(in);
        Recipe recipe = new Recipe();
        if (jsonRecipe.has("id"))
            recipe.setId(jsonRecipe.get("id").getAsInt());
        if (jsonRecipe.has("title"))
            recipe.setName(jsonRecipe.get("title").getAsString());
        if (jsonRecipe.has("image"))
            recipe.setImg(jsonRecipe.get("image").getAsString());
        if (jsonRecipe.has("readyInMinutes"))
            recipe.setPreparationTime(Math.max(jsonRecipe.get("readyInMinutes").getAsInt(), 0));
        if (jsonRecipe.has("extendedIngredients"))
            recipe.setIngredients(parseIngredients(jsonRecipe.get("extendedIngredients").getAsJsonArray()));
        if (jsonRecipe.has("analyzedInstructions"))
            recipe.setInstructions(parseInstructions(jsonRecipe.get("analyzedInstructions").getAsJsonArray()));

        return recipe;
    }

    private static List<String> parseInstructions(JsonArray instructionsJsonArray) {
        List<String> instructions = new ArrayList<>();
        if (instructionsJsonArray.size() > 0) {
            JsonArray steps = instructionsJsonArray.get(0).getAsJsonObject().get("steps").getAsJsonArray();
            for (int i = 0; i < steps.size(); i++) {
                instructions.add(steps.get(i).getAsJsonObject().get("step").getAsString());
            }
        }
        return instructions;
    }

    private static List<String> parseIngredients(JsonArray ingredientsJsonArray) {
        List<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsJsonArray.size(); i++) {
            ingredients.add(ingredientsJsonArray.get(i).getAsJsonObject().get("original").getAsString());
        }

        return ingredients;
    }
}