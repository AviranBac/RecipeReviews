package com.example.recipereviews.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();
    private List<Recipe> recipeList;

    public static Model instance(){
        return _instance;
    }
    private Model(){
        recipeList = new ArrayList<Recipe>() {{
            add(new Recipe(1, "The Best Chili", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
            add(new Recipe(2, "Sausage & Pepperoni Stromboli", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
            add(new Recipe(3, "Lasagna Roll Ups", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
        }};
    }

    public interface Listener<T>{
        void onComplete(T data);
    }


    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventRecipesListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);


    public List<Recipe> getAllRecipes() {
        return recipeList;
    }

    public void refreshAllStudents(){
        EventRecipesListLoadingState.setValue(LoadingState.LOADING);
    }

}
