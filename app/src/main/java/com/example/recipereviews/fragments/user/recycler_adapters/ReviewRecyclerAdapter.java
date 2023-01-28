package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.ReviewCardRowBinding;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.utils.ImageUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.function.Consumer;


class ReviewViewHolder extends GenericViewHolder<ReviewWithUser> {

    private ShapeableImageView reviewImage;
    private TextView reviewDescription;
    private TextView userNameTextView;
    private ShapeableImageView userImage;
    private RatingBar ratingBar;

    public ReviewViewHolder(ReviewCardRowBinding binding, Consumer<Integer> itemClickListener) {
        super(binding, itemClickListener);
    }

    @Override
    protected void initMembers() {
        ReviewCardRowBinding binding = (ReviewCardRowBinding) this.binding;
        this.reviewImage = binding.reviewImage;
        this.reviewDescription = binding.reviewDescriptionTv;
        this.ratingBar = binding.rating;
        this.userNameTextView = binding.userName;
        this.userImage = binding.userImage;
    }

    @Override
    public void bind(ReviewWithUser reviewWithUser) {
        if (reviewWithUser.getReview() != null) {
            this.reviewDescription.setText(reviewWithUser.getReview().getDescription());
            ImageUtil.loadImage(this.reviewImage, reviewWithUser.getReview().getImageUrl(), R.drawable.recipe_background);
            this.ratingBar.setRating((float) reviewWithUser.getReview().getRating());
        }

        if (reviewWithUser.getUser() != null) {
            ImageUtil.loadImage(this.userImage, reviewWithUser.getUser().getImageUrl(), R.drawable.blank_profile_picture);
            this.userNameTextView.setText(reviewWithUser.getUser().getFullName());
        }
    }
}

public class ReviewRecyclerAdapter extends GenericRecyclerAdapter<ReviewWithUser, ReviewViewHolder> {

    public ReviewRecyclerAdapter(LayoutInflater inflater, List<ReviewWithUser> data) {
        super(inflater, data);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewCardRowBinding binding = ReviewCardRowBinding.inflate(this.inflater, parent, false);
        return new ReviewViewHolder(binding, this.itemClickListener);
    }
}