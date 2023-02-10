package com.example.recipereviews.fragments.user;

import static com.example.recipereviews.fragments.user.RecipeDetailsFragmentDirections.actionGlobalReviewDetailsFragment;
import static com.example.recipereviews.fragments.user.RecipeDetailsFragmentDirections.actionGlobalSaveReviewFragment;
import static com.example.recipereviews.utils.BulletListUtil.buildBulletList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentRecipeDetailsBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.fragments.user.recycler_adapters.ReviewRecyclerAdapter;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.models.RecipeModel;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.viewModels.RecipeDetailsFragmentViewModel;
import com.example.recipereviews.viewModels.ReviewListViewModel;
import com.example.recipereviews.viewModels.SharedViewModel;
import com.example.recipereviews.viewModels.factory.RecipeDetailsFragmentViewModelFactory;
import com.example.recipereviews.viewModels.factory.ReviewListViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
import java.util.Objects;

public class RecipeDetailsFragment extends Fragment {
    private static final String RECIPE_ID_PARAM = "recipeId";
    private RecipeDetailsFragmentViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private int recipeId;
    private FragmentRecipeDetailsBinding binding;
    private TextView recipeNameTextView;
    private ShapeableImageView recipeImageView;
    private TextView preparationTimeTextView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private CircularProgressIndicator recipeProgressIndicator;
    private CircularProgressIndicator reviewsProgressIndicator;
    private FloatingActionButton addReviewButton;
    private ReviewRecyclerAdapter adapter;
    private ReviewListViewModel reviewListViewModel;
    private TextView reviewsSectionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeMenu();
        if (getArguments() != null) {
            this.recipeId = getArguments().getInt(RECIPE_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initMembers();
        this.setListener(view);
        this.addObservers();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            this.recipeId = getArguments().getInt(RECIPE_ID_PARAM);
            this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            this.viewModel = new ViewModelProvider(this, new RecipeDetailsFragmentViewModelFactory(this.recipeId)).get(RecipeDetailsFragmentViewModel.class);
            this.reviewListViewModel = new ViewModelProvider(this, new ReviewListViewModelFactory(this.recipeId)).get(ReviewListViewModel.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RecipeModel.getInstance().fetchRecipeById(this.recipeId);
        ReviewModel.getInstance().refreshReviewByRecipeId(this.recipeId);
    }

    private void initMembers() {
        this.recipeNameTextView = this.binding.recipeName;
        this.recipeImageView = this.binding.recipeImage;
        this.preparationTimeTextView = this.binding.preparationTime;
        this.ingredientsTextView = this.binding.ingredientsTextView;
        this.instructionsTextView = this.binding.instructionsTextView;
        this.recipeProgressIndicator = this.binding.recipeProgressIndicator;
        this.reviewsProgressIndicator = this.binding.reviewsProgressIndicator;
        this.addReviewButton = this.binding.addReviewButton;
        this.reviewsSectionTextView = this.binding.reviewsSectionTextView;
        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new ReviewRecyclerAdapter(getLayoutInflater(), this.reviewListViewModel.getReviewListDataByRecipeId().getValue());
        this.binding.recyclerView.setAdapter(this.adapter);
    }

    private void setListener(View view) {
        this.addReviewButton.setOnClickListener(view1 -> NavigationUtils.navigate(view, actionGlobalSaveReviewFragment(false)));
        this.adapter.setOnItemClickListener(pos -> {
            ReviewWithUser reviewWithUser = Objects.requireNonNull(this.reviewListViewModel.getReviewListDataByRecipeId().getValue()).get(pos);
            this.sharedViewModel.setReviewData(reviewWithUser.getReview());
            this.sharedViewModel.setUserData(reviewWithUser.getUser());
            NavigationUtils.navigate(view, actionGlobalReviewDetailsFragment());
        });
    }

    private void addObservers() {
        this.viewModel.getRecipeData().observe(getViewLifecycleOwner(), this::loadData);
        this.sharedViewModel.getReviewData().observe(getViewLifecycleOwner(), this::setEditButtonListener);
        this.reviewListViewModel.getReviewListDataByRecipeId().observe(getViewLifecycleOwner(), list -> {
            this.adapter.setData(list);
            this.updateCurrentUserReview(list);
            this.reviewsSectionTextView.setText(requireContext().getString(R.string.reviews_header, String.valueOf(list.size())));
        });

        RecipeModel.getInstance().getRecipeDetailsLoadingState().observe(
                getViewLifecycleOwner(),
                loadingState -> this.handleProgressIndicator(loadingState, this.recipeProgressIndicator)
        );
        ReviewModel.getInstance().getReviewListLoadingState().observe(
                getViewLifecycleOwner(),
                loadingState -> this.handleProgressIndicator(loadingState, this.reviewsProgressIndicator)
        );
    }

    private void handleProgressIndicator(LoadingState loadingState,
                                         CircularProgressIndicator circularProgressIndicator) {
        if (loadingState == LoadingState.LOADING) {
            this.binding.recipeDetailsLinearLayout.setVisibility(View.GONE);
            circularProgressIndicator.show();
        } else {
            circularProgressIndicator.hide();
            this.binding.recipeDetailsLinearLayout.setVisibility(View.VISIBLE);
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
                    sharedViewModel.setRecipeData(null);
                    sharedViewModel.setUserData(null);
                }

                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }


    private void loadData(Recipe recipe) {
        if (recipe != null) {
            this.recipeNameTextView.setText(recipe.getName());
            ImageUtil.loadImage(this.recipeImageView, recipe.getImg(), R.drawable.recipe_background);
            this.preparationTimeTextView.setText(requireContext().getString(R.string.preparation_time, String.valueOf(recipe.getPreparationTime())));
            this.ingredientsTextView.setText(buildBulletList(20, recipe.getIngredients()));
            this.instructionsTextView.setText(buildBulletList(20, recipe.getInstructions()));
            this.sharedViewModel.setRecipeData(recipe);
        }
    }

    private void updateCurrentUserReview(List<ReviewWithUser> reviews) {
        reviews.stream()
                .filter(reviewWithUser -> !reviewWithUser.getReview().isDeleted() &&
                        reviewWithUser.getUser().getId().equals(UserModel.getInstance().getCurrentUserId()) &&
                        this.recipeId == reviewWithUser.getReview().getRecipeId())
                .findAny()
                .ifPresent(review -> this.sharedViewModel.setReviewData(review.getReview()));
    }

    private void setEditButtonListener(Review review) {
        boolean isEdit = review != null;
        int reviewButtonImageResource = review != null ? R.drawable.baseline_edit_24 : R.drawable.baseline_add_24;
        this.addReviewButton.setOnClickListener(view -> NavigationUtils.navigate(view, actionGlobalSaveReviewFragment(isEdit)));
        this.addReviewButton.setImageResource(reviewButtonImageResource);
    }
}