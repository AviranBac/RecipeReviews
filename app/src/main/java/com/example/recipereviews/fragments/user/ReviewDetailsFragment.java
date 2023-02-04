package com.example.recipereviews.fragments.user;


import static com.example.recipereviews.fragments.user.ReviewDetailsFragmentDirections.actionGlobalSaveReviewFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentReviewDetailsBinding;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.viewModels.SharedViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.function.Consumer;

public class ReviewDetailsFragment extends Fragment {

    private FragmentReviewDetailsBinding binding;
    private SharedViewModel sharedViewModel;
    private TextView recipeNameTextView;
    private ShapeableImageView reviewImage;
    private ShapeableImageView userImage;
    private TextView userName;
    private RatingBar ratingBar;
    private TextView reviewDescriptionTextView;
    private Button editButton;
    private Button deleteButton;
    private CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentReviewDetailsBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initializeMembers();
        this.setListeners();
        this.addObservers();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    private void initializeMembers() {
        this.recipeNameTextView = this.binding.recipeName;
        this.reviewImage = this.binding.reviewImage;
        this.userImage = this.binding.userImage;
        this.userName = this.binding.userName;
        this.ratingBar = this.binding.rating;
        this.reviewDescriptionTextView = this.binding.reviewDescription;
        this.editButton = this.binding.editButton;
        this.deleteButton = this.binding.deleteButton;
        this.progressIndicator = this.binding.progressIndicator;
    }

    private void setListeners() {
        this.editButton.setOnClickListener(view -> NavigationUtils.navigate(view, actionGlobalSaveReviewFragment(true)));
        this.deleteButton.setOnClickListener(this::deleteReview);
    }

    private void addObservers() {
        this.sharedViewModel.getRecipeData().observe(getViewLifecycleOwner(), this::loadRecipeData);
        this.sharedViewModel.getReviewData().observe(getViewLifecycleOwner(), this::loadReviewData);
        this.sharedViewModel.getUserData().observe(getViewLifecycleOwner(), this::loadUserData);

    }

    private void loadRecipeData(Recipe recipe) {
        if (recipe != null) {
            this.recipeNameTextView.setText(requireContext().getString(R.string.review_name_placeholder, String.valueOf(recipe.getName())));
        }
    }

    private void loadReviewData(Review review) {
        if (review != null) {
            ImageUtil.loadImage(this.reviewImage, review.getImageUrl(), R.drawable.blank_review_image);
            this.reviewDescriptionTextView.setText(review.getDescription());
            this.ratingBar.setRating((float) review.getRating());
            if (review.getUserId().equals(UserModel.getInstance().getCurrentUserId())) {
                this.editButton.setVisibility(View.VISIBLE);
                this.deleteButton.setVisibility(View.VISIBLE);
            }
        }

    }

    private void loadUserData(User user) {
        if (user != null) {
            ImageUtil.loadImage(this.userImage, user.getImageUrl(), R.drawable.blank_profile_picture);
            this.userName.setText(user.getFullName());
        }
    }

    private void deleteReview(View view) {
        String deleteButtonText = this.deleteButton.getText().toString();
        this.deleteButton.setEnabled(false);
        this.deleteButton.setText("");
        this.editButton.setEnabled(false);
        this.progressIndicator.show();
        Review review = this.sharedViewModel.getReviewData().getValue();
        if (review != null) {
            ReviewModel.getInstance().deleteReview(review, this.getSuccessListener(), this.getErrorListener(view, deleteButtonText));
        }
    }

    private void initializeMenu() {
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.empty_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    sharedViewModel.setReviewData(null);
                }

                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    private Consumer<Review> getSuccessListener() {
        return (review) -> {
            this.progressIndicator.hide();
            this.sharedViewModel.setReviewData(null);
            NavigationUtils.navigate(requireActivity(), NavController::navigateUp);
        };
    }

    private Consumer<String> getErrorListener(View view, String deleteButtonText) {
        return errorMessage -> {
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
            this.progressIndicator.hide();
            this.deleteButton.setEnabled(true);
            this.deleteButton.setText(deleteButtonText);
        };
    }

}