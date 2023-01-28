package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.recipereviews.databinding.ProfileReviewCardRowBinding;
import com.example.recipereviews.models.entities.ReviewWithRecipe;

import java.util.List;
import java.util.function.Consumer;


class ProfileReviewViewHolder extends GenericViewHolder<ReviewWithRecipe> {

    private TextView recipeNameTv;
    private RatingBar ratingBar;

    public ProfileReviewViewHolder(ProfileReviewCardRowBinding binding, Consumer<Integer> itemClickListener) {
        super(binding, itemClickListener);
    }

    @Override
    protected void initMembers() {
        ProfileReviewCardRowBinding binding = (ProfileReviewCardRowBinding) this.binding;
        this.recipeNameTv = binding.recipeNameTv;
        this.ratingBar = binding.rating;
    }

    @Override
    public void bind(ReviewWithRecipe reviewWithRecipe) {
        this.recipeNameTv.setText(reviewWithRecipe.getRecipe().getName());
        this.ratingBar.setRating((float) reviewWithRecipe.getReview().getRating());
    }
}

public class ProfileReviewRecyclerAdapter extends GenericRecyclerAdapter<ReviewWithRecipe, ProfileReviewViewHolder> {

    public ProfileReviewRecyclerAdapter(LayoutInflater inflater, List<ReviewWithRecipe> data) {
        super(inflater, data);
    }

    @NonNull
    @Override
    public ProfileReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProfileReviewCardRowBinding binding = ProfileReviewCardRowBinding.inflate(this.inflater, parent, false);
        return new ProfileReviewViewHolder(binding, this.itemClickListener);
    }
}