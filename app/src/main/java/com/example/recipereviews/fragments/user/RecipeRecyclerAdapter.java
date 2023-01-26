package com.example.recipereviews.fragments.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.RecipeCardRowBinding;
import com.example.recipereviews.interfaces.OnItemClickListener;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.utils.ImageUtil;

import java.util.List;


class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView recipeNameTv;
    ImageView recipeImageIv;
    RecipeCardRowBinding binding;

    public RecipeViewHolder(RecipeCardRowBinding binding, OnItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.initMembers();
        this.setListeners(listener);
    }

    private void initMembers() {
        this.recipeNameTv = this.binding.recipeNameTv;
        this.recipeImageIv = this.binding.recipeImage;
    }

    private void setListeners(OnItemClickListener listener) {
        this.binding.getRoot().setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    public void bind(Recipe recipe) {
        this.recipeNameTv.setText(recipe.getName());
        ImageUtil.loadImage(this.recipeImageIv, recipe.getImg(), R.drawable.recipe_background);
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    OnItemClickListener listener;
    LayoutInflater inflater;
    List<Recipe> data;

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data) {
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Recipe> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeCardRowBinding binding = RecipeCardRowBinding.inflate(this.inflater, parent, false);
        return new RecipeViewHolder(binding, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        if (this.data == null) return 0;
        return this.data.size();
    }
}

