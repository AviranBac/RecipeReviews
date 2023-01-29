package com.example.recipereviews.viewModels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipereviews.viewModels.EditProfileFragmentViewModel;

import java.util.Objects;

public class EditProfileFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final String userId;

    public EditProfileFragmentViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new EditProfileFragmentViewModel(this.userId)));
    }
}
