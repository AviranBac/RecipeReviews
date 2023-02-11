package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.RecipeCardRowBinding;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.utils.ImageUtils;

import java.util.List;
import java.util.function.Consumer;

class RecipeViewHolder extends GenericViewHolder<Recipe> {

    private TextView recipeNameTv;
    private ImageView recipeImageIv;

    public RecipeViewHolder(RecipeCardRowBinding binding, Consumer<Integer> itemClickListener) {
        super(binding, itemClickListener);
    }

    @Override
    protected void initMembers() {
        RecipeCardRowBinding binding = (RecipeCardRowBinding) this.binding;
        this.recipeNameTv = binding.recipeNameTv;
        this.recipeImageIv = binding.recipeImage;
    }

    @Override
    public void bind(Recipe recipe) {
        this.recipeNameTv.setText(recipe.getName());
        ImageUtils.loadImage(this.recipeImageIv, recipe.getImg(), R.drawable.recipe_background);
    }
}

public class RecipeRecyclerAdapter extends GenericRecyclerAdapter<Recipe, RecipeViewHolder> {

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data) {
        super(inflater, data);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeCardRowBinding binding = RecipeCardRowBinding.inflate(super.inflater, parent, false);
        return new RecipeViewHolder(binding, this.itemClickListener);
    }
}

