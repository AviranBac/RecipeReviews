package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.UserModel;

public class EditProfileFragmentViewModel extends ViewModel {
    private LiveData<User> profileUser;

    public EditProfileFragmentViewModel(String userId) {
        this.profileUser = UserModel.getInstance().getUserById(userId);
    }

    public LiveData<User> getProfileUser() {
        return this.profileUser;
    }
}
