package com.example.recipereviews;

import androidx.lifecycle.ViewModel;

import com.example.recipereviews.model.Model;
import com.example.recipereviews.model.Recipe;

import java.util.List;

public class MainPageFragmentViewModal extends ViewModel {
    private List<Recipe> data = Model.instance().getAllRecipes();

    List<Recipe> getData(){
        return data;
    }
}
