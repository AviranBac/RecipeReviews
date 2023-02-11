package com.example.recipereviews.models.models;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.ReviewWithRecipe;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ReviewModel {
    private static final ReviewModel instance = new ReviewModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final MutableLiveData<LoadingState> reviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> profileReviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> reviewLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final ModelFirebase firebaseModel = new ModelFirebase();
    private final MutableLiveData<List<ReviewWithUser>> reviewList = new MutableLiveData<>();
    private MutableLiveData<List<ReviewWithRecipe>> profileReviewList = new MutableLiveData<>();

    private ReviewModel() {
    }

    public static ReviewModel getInstance() {
        return instance;
    }

    public MutableLiveData<LoadingState> getReviewListLoadingState() {
        return this.reviewListLoadingState;
    }

    public MutableLiveData<LoadingState> getProfileReviewListLoadingState() {
        return this.profileReviewListLoadingState;
    }

    public MutableLiveData<LoadingState> getReviewLoadingState() {
        return this.reviewLoadingState;
    }

    public MutableLiveData<List<ReviewWithUser>> getReviewByRecipeId(int recipeId) {
        if (this.reviewList.getValue() == null) {
            this.refreshReviewsByRecipeId(recipeId);
        }

        return this.reviewList;
    }

    public void refreshReviewsByRecipeId(int recipeId) {
        this.reviewListLoadingState.setValue(LoadingState.LOADING);
        long reviewLastUpdateTime = Review.getLocalLastUpdateTime();

        UserModel.getInstance().refreshUserList((unused) ->
                this.firebaseModel.getReviewsSince(reviewLastUpdateTime, list -> executor.execute(() -> {
                    this.handleRefreshedReviews(list, reviewLastUpdateTime);
                    this.reviewList.postValue(this.localDb.reviewDao().getByRecipeId(recipeId));
                    this.reviewListLoadingState.postValue(LoadingState.NOT_LOADING);
                }))
        );
    }

    public void refreshReview(Review review, User user, Consumer<ReviewWithUser> callback) {
        this.reviewLoadingState.setValue(LoadingState.LOADING);
        long reviewLastUpdateTime = Review.getLocalLastUpdateTime();

        UserModel.getInstance().refreshUserList((users) ->
                this.firebaseModel.getReviewsSince(reviewLastUpdateTime, list -> executor.execute(() -> {
                    this.handleRefreshedReviews(list, reviewLastUpdateTime);
                    ReviewWithUser reviewWithUser = new ReviewWithUser();
                    Review r = list.stream().filter(itReview -> itReview.getId().equals(review.getId())).findAny().orElse(review);
                    if (r.isDeleted()) {
                        reviewWithUser.setReview(null);
                    } else {
                        reviewWithUser.setReview(r);
                    }
                    reviewWithUser.setUser(users.stream().filter(u -> u.getId().equals(user.getId())).findAny().orElse(user));
                    callback.accept(reviewWithUser);
                    this.reviewLoadingState.postValue(LoadingState.NOT_LOADING);
                }))
        );
    }


    public LiveData<List<ReviewWithRecipe>> getLoggedInUserReviews() {
        if (this.profileReviewList.getValue() == null) {
            this.refreshLoggedInUserReviews();
        }

        return this.profileReviewList;
    }

    public void refreshLoggedInUserReviews() {
        this.profileReviewListLoadingState.setValue(LoadingState.LOADING);
        long reviewLastUpdateTime = Review.getLocalLastUpdateTime();

        UserModel.getInstance().refreshLoggedInUser(() ->
                this.firebaseModel.getReviewsSince(reviewLastUpdateTime, list -> executor.execute(() -> {
                    this.handleRefreshedReviews(list, reviewLastUpdateTime);
                    this.profileReviewList.postValue(this.localDb.reviewDao().getByUserId(UserModel.getInstance().getCurrentUserId()));
                    this.profileReviewListLoadingState.postValue(LoadingState.NOT_LOADING);
                }))
        );
    }

    private void handleRefreshedReviews(List<Review> refreshedReviews, long reviewLastUpdateTime) {
        long latestLastUpdateTime = refreshedReviews.stream()
                .mapToLong(Review::getLastUpdateTime)
                .max()
                .orElse(reviewLastUpdateTime);

        refreshedReviews.forEach(review -> {
            localDb.reviewDao().insertAll(review);
            if (review.isDeleted()) {
                localDb.reviewDao().delete(review);
            }
        });

        Review.setLocalLastUpdateTime(latestLastUpdateTime);
    }

    public void uploadReviewImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        this.firebaseModel.uploadReviewImage(imageBitmap, name, imageUploadCallback);
    }

    public void editReview(Review review, Consumer<Review> onEditSuccessListener, Consumer<String> onEditFailureListener) {
        this.firebaseModel.editReview(review, () -> onEditSuccessListener.accept(review), onEditFailureListener);
    }

    public void addReview(Review review, Consumer<Review> onAddSuccessListener, Consumer<String> onAddFailureListener) {
        this.firebaseModel.addReview(review, (String reviewId) -> {
            review.setId(reviewId);
            onAddSuccessListener.accept(review);
        }, onAddFailureListener);
    }

    public void deleteReview(Review review, Consumer<Review> onDeleteSuccessListener, Consumer<String> onDeleteFailureListener) {
        review.setDeleted(true);
        this.editReview(review, onDeleteSuccessListener, onDeleteFailureListener);
    }
}
