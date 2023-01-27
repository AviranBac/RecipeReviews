package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.RecipeCardRowBinding;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.utils.ImageUtil;

import java.util.List;
import java.util.function.Consumer;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    private TextView recipeNameTv;
    private ImageView recipeImageIv;
    private RecipeCardRowBinding binding;

    public RecipeViewHolder(RecipeCardRowBinding binding, Consumer<Integer> itemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.initMembers();
        this.setListeners(itemClickListener);
    }

    private void initMembers() {
        this.recipeNameTv = this.binding.recipeNameTv;
        this.recipeImageIv = this.binding.recipeImage;
    }

    private void setListeners(Consumer<Integer> itemClickListener) {
        this.binding.getRoot().setOnClickListener(view ->
                itemClickListener.accept(getAdapterPosition())
        );
    }

    public void bind(Recipe recipe) {
        this.recipeNameTv.setText(recipe.getName());
        ImageUtil.loadImage(this.recipeImageIv, recipe.getImg(), R.drawable.recipe_background);
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private Consumer<Integer> itemClickListener;
    private LayoutInflater inflater;
    private List<Recipe> data;

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data) {
        this.inflater = inflater;
        this.data = data;
    }

    public void setOnItemClickListener(Consumer<Integer> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<Recipe> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeCardRowBinding binding = RecipeCardRowBinding.inflate(this.inflater, parent, false);
        return new RecipeViewHolder(binding, this.itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }
}

