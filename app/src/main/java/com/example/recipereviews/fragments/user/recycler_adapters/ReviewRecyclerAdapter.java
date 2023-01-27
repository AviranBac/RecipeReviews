package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.ReviewCardRowBinding;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.utils.ImageUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.function.Consumer;


class ReviewViewHolder extends RecyclerView.ViewHolder {
    private final ReviewCardRowBinding binding;
    private ShapeableImageView reviewImage;
    private TextView reviewDescription;
    private TextView userNameTextView;
    private ShapeableImageView userImage;
    private RatingBar ratingBar;

    public ReviewViewHolder(ReviewCardRowBinding binding, Consumer<Integer> itemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.initMembers();
        this.setListener(itemClickListener);
    }

    private void initMembers() {
        this.reviewImage = this.binding.reviewImage;
        this.reviewDescription = this.binding.reviewDescriptionTv;
        this.ratingBar = this.binding.rating;
        this.userNameTextView = this.binding.userName;
        this.userImage = this.binding.userImage;
    }

    private void setListener(Consumer<Integer> itemClickListener) {
        this.binding.getRoot().setOnClickListener(view ->
                itemClickListener.accept(getAdapterPosition())
        );
    }

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

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private Consumer<Integer> itemClickListener;
    private ReviewCardRowBinding binding;
    private LayoutInflater inflater;
    private List<ReviewWithUser> data;

    public ReviewRecyclerAdapter(LayoutInflater inflater, List<ReviewWithUser> data) {
        this.inflater = inflater;
        this.data = data;
    }

    public void setOnItemClickListener(Consumer<Integer> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<ReviewWithUser> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.binding = ReviewCardRowBinding.inflate(this.inflater, parent, false);
        return new ReviewViewHolder(this.binding, this.itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewWithUser reviewWithUser = this.data.get(position);
        holder.bind(reviewWithUser);
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }
}