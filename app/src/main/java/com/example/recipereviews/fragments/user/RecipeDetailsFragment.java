package com.example.recipereviews.fragments.user;

import static com.example.recipereviews.utils.BulletListUtil.buildBulletList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentRecipeDetailsBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.RecipeDetailsModel;
import com.example.recipereviews.models.models.ReviewListModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.viewModels.RecipeDetailsFragmentViewModel;
import com.example.recipereviews.viewModels.factory.RecipeDetailsFragmentViewModelFactory;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class RecipeDetailsFragment extends Fragment {
    private static final String RECIPE_ID_PARAM = "recipeId";
    private RecipeDetailsFragmentViewModel viewModel;
    private int recipeId;
    private FragmentRecipeDetailsBinding binding;
    private TextView recipeNameTextView;
    private ShapeableImageView recipeImageView;
    private TextView preparationTimeTextView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private CircularProgressIndicator recipeProgressIndicator;
    private CircularProgressIndicator reviewsProgressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        this.addObservers();
        this.displayReviewList();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            this.recipeId = getArguments().getInt(RECIPE_ID_PARAM);
            this.viewModel = new ViewModelProvider(this, new RecipeDetailsFragmentViewModelFactory(this.recipeId)).get(RecipeDetailsFragmentViewModel.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RecipeDetailsModel.getInstance().fetchRecipeById(this.recipeId);
    }

    private void initMembers() {
        this.recipeNameTextView = this.binding.recipeName;
        this.recipeImageView = this.binding.recipeImage;
        this.preparationTimeTextView = this.binding.preparationTime;
        this.ingredientsTextView = this.binding.ingredientsTextView;
        this.instructionsTextView = this.binding.instructionsTextView;
        this.recipeProgressIndicator = this.binding.recipeProgressIndicator;
        this.reviewsProgressIndicator = this.binding.reviewsProgressIndicator;
    }

    private void addObservers() {
        this.viewModel.getRecipeData().observe(getViewLifecycleOwner(), this::loadData);

        RecipeDetailsModel.getInstance().getEventRecipesDetailsLoadingState().observe(
                getViewLifecycleOwner(),
                loadingState -> this.handleProgressIndicator(loadingState, this.binding.recipeDetailsLinearLayout, this.recipeProgressIndicator)
        );
        ReviewListModel.getInstance().getEventReviewListLoadingState().observe(
                getViewLifecycleOwner(),
                loadingState -> this.handleProgressIndicator(loadingState, this.binding.reviewList, this.reviewsProgressIndicator)
        );
    }

    private void handleProgressIndicator(LoadingState loadingState,
                                         View affectedView,
                                         CircularProgressIndicator circularProgressIndicator) {
        if (loadingState == LoadingState.LOADING) {
            affectedView.setVisibility(View.GONE);
            circularProgressIndicator.show();
        } else {
            affectedView.setVisibility(View.VISIBLE);
            circularProgressIndicator.hide();
        }
    }

    private void displayReviewList() {
        Fragment reviewListFragment = ReviewListFragment.newInstance(this.recipeId);
        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.add(this.binding.reviewList.getId(), reviewListFragment);
        tran.addToBackStack("TAG");
        tran.commit();
    }

    private void loadData(Recipe recipe) {
        if (recipe != null) {
            this.recipeNameTextView.setText(recipe.getName());
            ImageUtil.loadImage(this.recipeImageView, recipe.getImg(), R.drawable.recipe_background);
            this.preparationTimeTextView.setText(requireContext().getString(R.string.preparation_time, String.valueOf(recipe.getPreparationTime())));
            this.ingredientsTextView.setText(buildBulletList(20, recipe.getIngredients()));
            this.instructionsTextView.setText(buildBulletList(20, recipe.getInstructions()));
        }
    }
}