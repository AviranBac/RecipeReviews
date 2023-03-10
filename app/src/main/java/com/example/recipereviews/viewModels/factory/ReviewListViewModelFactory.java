package com.example.recipereviews.viewModels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipereviews.viewModels.ReviewListViewModel;

import java.util.Objects;

public class ReviewListViewModelFactory implements ViewModelProvider.Factory {
    private final int recipeId;

    public ReviewListViewModelFactory(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new ReviewListViewModel(this.recipeId)));
    }
}
