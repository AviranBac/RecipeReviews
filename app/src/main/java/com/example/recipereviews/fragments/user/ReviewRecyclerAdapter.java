package com.example.recipereviews.fragments.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.ReviewCardRowBinding;
import com.example.recipereviews.interfaces.OnItemClickListener;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.utils.ImageUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;


class ReviewViewHolder extends RecyclerView.ViewHolder {
    private final ReviewCardRowBinding binding;
    private ShapeableImageView reviewImage;
    private TextView reviewDescription;

    private TextView userNameTextView;
    private ShapeableImageView userImage;
    private RatingBar ratingBar;

    public ReviewViewHolder(ReviewCardRowBinding binding, OnItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.initMembers();
        this.setListener(listener);
    }

    private void initMembers() {
        this.reviewImage = this.binding.reviewImage;
        this.reviewDescription = this.binding.reviewDescriptionTv;
        this.ratingBar = this.binding.rating;
        this.userNameTextView = this.binding.userName;
        this.userImage = this.binding.userImage;
    }

    private void setListener(OnItemClickListener listener) {
        this.binding.getRoot().setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    public void bind(ReviewWithUser reviewWithUser) {
        if (reviewWithUser.review != null) {
            this.reviewDescription.setText(reviewWithUser.review.getDescription());
            ImageUtil.loadImage(this.reviewImage, reviewWithUser.review.getImageUrl(), R.drawable.recipe_background);
            this.ratingBar.setRating((float) reviewWithUser.review.getRating());
        }
        if (reviewWithUser.user != null) {
            ImageUtil.loadImage(this.userImage, reviewWithUser.user.getImageUrl(), R.drawable.blank_profile_picture);
            this.userNameTextView.setText(reviewWithUser.user.getFullName());
        }
    }
}

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    OnItemClickListener listener;
    ReviewCardRowBinding binding;
    LayoutInflater inflater;
    List<ReviewWithUser> data;

    public ReviewRecyclerAdapter(LayoutInflater inflater, List<ReviewWithUser> data) {
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<ReviewWithUser> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.binding = ReviewCardRowBinding.inflate(this.inflater, parent, false);
        return new ReviewViewHolder(this.binding, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewWithUser reviewWithUser = this.data.get(position);
        holder.bind(reviewWithUser);
    }

    @Override
    public int getItemCount() {
        if (this.data == null) return 0;
        return this.data.size();
    }
}