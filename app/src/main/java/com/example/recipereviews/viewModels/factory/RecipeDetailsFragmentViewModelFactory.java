package com.example.recipereviews.viewModels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipereviews.viewModels.RecipeDetailsFragmentViewModel;

import java.util.Objects;

public class RecipeDetailsFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final int recipeId;

    public RecipeDetailsFragmentViewModelFactory(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new RecipeDetailsFragmentViewModel(recipeId)));
    }
}
