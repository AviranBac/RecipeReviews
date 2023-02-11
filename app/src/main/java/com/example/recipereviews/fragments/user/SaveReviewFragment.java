package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentSaveReviewBinding;
import com.example.recipereviews.fragments.common.CameraUtilsFragment;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtils;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.utils.ValidatorListenerUtils;
import com.example.recipereviews.validators.InputValidator;
import com.example.recipereviews.viewModels.SharedViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.function.Consumer;

public class SaveReviewFragment extends CameraUtilsFragment {

    private final String IS_EDIT_PARAM = "isEdit";
    private boolean isEdit;

    private FragmentSaveReviewBinding binding;
    private TextView recipeNameTextView;
    private RatingBar ratingBar;
    private TextInputEditText reviewDescriptionEditText;
    private TextInputLayout reviewDescriptionTextInput;
    private SharedViewModel sharedViewModel;
    private Button saveButton;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaveReviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        this.initializeMembers();
        this.addObservers();
        this.setListener();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            this.isEdit = getArguments().getBoolean(IS_EDIT_PARAM);
            this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        }
    }

    private void initializeMembers() {
        super.imageView = this.binding.reviewImage;
        super.defaultPicture = R.drawable.blank_review_image;
        this.recipeNameTextView = this.binding.recipeName;
        this.ratingBar = this.binding.rating;
        this.reviewDescriptionTextInput = this.binding.reviewDescriptionTextInput;
        this.reviewDescriptionEditText = this.binding.reviewDescription;
        this.saveButton = this.binding.saveButton;
        this.progressIndicator = this.binding.progressIndicator;
    }

    private void setListener() {
        ValidatorListenerUtils.setOnKeyListener(reviewDescriptionEditText, this::validateDescription);
        super.imageView.setOnClickListener(super::showCameraMenu);
        this.saveButton.setOnClickListener(view -> {
            if (this.validateDescription()) {
                this.saveReview(view);
            }
        });
    }

    private void addObservers() {
        this.sharedViewModel.getRecipeData().observe(getViewLifecycleOwner(), this::loadRecipeData);
        if (this.isEdit) {
            this.sharedViewModel.getReviewData().observe(getViewLifecycleOwner(), this::loadReview);
        }
    }

    private void loadRecipeData(Recipe recipe) {
        if (recipe != null) {
            this.recipeNameTextView.setText(requireContext().getString(R.string.review_name_placeholder, String.valueOf(recipe.getName())));
        }
    }

    private void loadReview(Review review) {
        if (review != null) {
            ImageUtils.loadImage(super.imageView, review.getImageUrl(), R.drawable.blank_review_image);
            this.reviewDescriptionEditText.setText(review.getDescription());
            this.ratingBar.setRating((float) review.getRating());
        }
    }

    private void saveReview(View view) {
        String saveButtonText = this.saveButton.getText().toString();
        this.saveButton.setEnabled(false);
        this.saveButton.setText("");
        this.progressIndicator.show();

        String userId = UserModel.getInstance().getCurrentUserId();

        Review review = new Review(
                Objects.requireNonNull(this.sharedViewModel.getRecipeData().getValue()).getId(),
                Objects.requireNonNull(userId),
                this.ratingBar.getRating(),
                Objects.requireNonNull(this.reviewDescriptionEditText.getText()).toString()
        );

        Drawable reviewImage = super.imageView.getDrawable();
        if (reviewImage == null || reviewImage.getConstantState().equals(requireContext().getDrawable(R.drawable.blank_review_image).getConstantState())) {
            this.saveReviewHandler(review, view, saveButtonText);
        } else {
            super.imageView.setDrawingCacheEnabled(true);
            super.imageView.buildDrawingCache();
            ReviewModel.getInstance().uploadReviewImage(((BitmapDrawable) reviewImage).getBitmap(), review.getRecipeId() + userId, (String url) -> {
                review.setImageUrl(url);
                this.saveReviewHandler(review, view, saveButtonText);
            });
        }
    }

    private void saveReviewHandler(Review review, View view, String saveButtonText) {
        if (this.isEdit) {
            review.setId(Objects.requireNonNull(this.sharedViewModel.getReviewData().getValue()).getId());
            ReviewModel.getInstance().editReview(review, this.getSuccessListener(), this.getErrorListener(view, saveButtonText));
        } else {
            ReviewModel.getInstance().addReview(review, this.getSuccessListener(), this.getErrorListener(view, saveButtonText));
        }
    }

    private Consumer<Review> getSuccessListener() {
        return (review) -> {
            progressIndicator.hide();
            this.imageView.setImageResource(R.drawable.blank_review_image);
            this.reviewDescriptionEditText.setText(null);
            this.ratingBar.setRating(0);
            if (review != null) {
                this.sharedViewModel.setReviewData(review);
                this.sharedViewModel.setUserData(UserModel.getInstance().getLoggedInUser().getValue());
            }
            NavigationUtils.navigate(requireActivity(), NavController::navigateUp);
        };
    }

    private Consumer<String> getErrorListener(View view, String saveButtonText) {
        return errorMessage -> {
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
            progressIndicator.hide();
            this.saveButton.setEnabled(true);
            this.saveButton.setText(saveButtonText);
        };
    }

    private boolean validateDescription() {
        if (InputValidator.isDescriptionValid(this.reviewDescriptionEditText.getText())) {
            this.reviewDescriptionTextInput.setError(null);
            return true;
        } else {
            this.reviewDescriptionTextInput.setError(getString(R.string.required_field));
            return false;
        }
    }

}