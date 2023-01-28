package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.api.GetRecipesDto;
import com.example.recipereviews.models.api.RecipeApi;
import com.example.recipereviews.models.api.RecipeTypeAdapter;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeModel {
    final String BASE_URL = "https://api.spoonacular.com/";

    private static final RecipeModel instance = new RecipeModel();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<LoadingState> recipeDetailsLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> recipeListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
    private LiveData<List<Recipe>> recipeList;
    private Retrofit retrofit;
    private RecipeApi recipeApi;

    private RecipeModel() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Recipe.class, new RecipeTypeAdapter())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        recipeApi = retrofit.create(RecipeApi.class);
    }

    public static RecipeModel getInstance() {
        return instance;
    }

    public MutableLiveData<LoadingState> getRecipeDetailsLoadingState() {
        return this.recipeDetailsLoadingState;
    }

    public MutableLiveData<LoadingState> getRecipeListLoadingState() {
        return this.recipeListLoadingState;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        if (this.recipeList == null) {
            this.recipeList = this.localDb.recipeDao().getAll();
            refreshAllRecipes();
        }

        return this.recipeList;
    }

    public LiveData<Recipe> getRecipeById(int id) {
        if (this.recipe.getValue() == null) {
            this.fetchRecipeById(id);
        }

        return this.recipe;
    }

    public LiveData<List<Recipe>> getRecipesBySearchText(String text) {
        return this.localDb.recipeDao().getByName("%" + text + "%");
    }

    public void refreshAllRecipes() {
        this.recipeListLoadingState.setValue(LoadingState.LOADING);
        this.recipeApi.getRecipes().enqueue(new Callback<GetRecipesDto>() {
            @Override
            public void onResponse(Call<GetRecipesDto> call, Response<GetRecipesDto> recipesResponse) {
                if (recipesResponse.isSuccessful()) {
                    executor.execute(() -> {
                        if (recipesResponse.isSuccessful()) {
                            recipesResponse.body().getResults().forEach(recipe -> localDb.recipeDao().insertAll(recipe));
                            recipeListLoadingState.postValue(LoadingState.NOT_LOADING);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetRecipesDto> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchRecipeById(int id) {
        this.recipeDetailsLoadingState.setValue(LoadingState.LOADING);
        this.recipeApi.getRecipeById(id).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> recipeResponse) {
                executor.execute(() -> {
                    if (recipeResponse.isSuccessful()) {
                        localDb.recipeDao().insertAll(recipeResponse.body());
                        recipe.postValue(localDb.recipeDao().getById(id));
                        recipeDetailsLoadingState.postValue(LoadingState.NOT_LOADING);
                    }
                });
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
