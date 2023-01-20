package com.example.recipereviews;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipereviews.model.Recipe;
import com.squareup.picasso.Picasso;
import java.util.List;


class RecipeViewHolder extends RecyclerView.ViewHolder {
    List<Recipe> data;
    TextView recipeNameTv;
    ImageView recipeImageIv;

    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener, List<Recipe> data) {
        super(itemView);
        this.data = data;
        recipeNameTv = itemView.findViewById(R.id.recipe_name_tv);
        recipeImageIv = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Recipe recipe, int pos) {
        recipeNameTv.setText(recipe.getName());
        System.out.println(recipe.getImg());
        if(!recipe.getImg().equals("")) {
            Picasso.get().load(Uri.parse(recipe.getImg())).into(recipeImageIv);
        }
        else {
            recipeImageIv.setImageResource(R.drawable.recipe_background);
        }
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data) {
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_card_row, parent, false);
        return new RecipeViewHolder(view, listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.bind(recipe, position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

}

