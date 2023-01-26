package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.databinding.FragmentMainPageBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.Model;
import com.example.recipereviews.models.models.RecipesListModel;
import com.example.recipereviews.viewModels.MainPageFragmentViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class MainPageFragment extends Fragment {

    private ShapeableImageView profileImageView;
    private ShapeableImageView logoutImageView;
    private EditText searchEditText;
    private RecipeRecyclerAdapter adapter;
    private MainPageFragmentViewModel viewModel;
    private FragmentMainPageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initializeMembers();
        this.setListeners(view);
        this.loadData();
        return view;
    }

    private void loadData() {
        this.viewModel.getRecipeListData().observe(getViewLifecycleOwner(), list -> this.adapter.setData(list));
        RecipesListModel.getInstance().EventRecipesListLoadingState.observe(getViewLifecycleOwner(), status -> this.binding.swipeRefresh.setRefreshing(status == LoadingState.LOADING));
    }

    private void initializeMembers() {
        this.profileImageView = this.binding.profileImageView;
        this.logoutImageView = this.binding.logoutImageView;
        this.searchEditText = this.binding.searchEt;
        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new RecipeRecyclerAdapter(getLayoutInflater(), this.viewModel.getRecipeListData().getValue());
        this.binding.recyclerView.setAdapter(this.adapter);
    }

    private void setListeners(View view) {
        // TODO: need to set profile image from current user's imageUrl
        this.setProfileImageClickListener(view);
        this.setLogoutButtonClickListener();
        this.setSearchEditTextChangeListener();
        this.setOnRecipeClickListener(view);
        this.setOnRefreshListener();
    }

    private void setSearchEditTextChangeListener() {
        this.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = searchEditText.getText().toString();
                viewModel.setNameQuery(text);
                viewModel.getSearchedRecipes().observe(getViewLifecycleOwner(), list -> adapter.setData(list));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setProfileImageClickListener(View view) {
        this.profileImageView.setOnClickListener((View view1) -> Navigation.findNavController(view).navigate(MainPageFragmentDirections.actionMainPageFragmentToProfileFragment(Model.getInstance().getCurrentUserId())));
    }

    private void setLogoutButtonClickListener() {
        this.logoutImageView.setOnClickListener(view -> Model.getInstance().logout(this::startGuestActivity));
    }

    private void setOnRecipeClickListener(View view) {
        this.adapter.setOnItemClickListener(pos -> {
            Recipe recipe = Objects.requireNonNull(this.viewModel.getRecipeListData().getValue()).get(pos);
            MainPageFragmentDirections.ActionMainPageFragmentToRecipeDetailsFragment action = MainPageFragmentDirections.actionMainPageFragmentToRecipeDetailsFragment(recipe.getId());
            Navigation.findNavController(view).navigate(action);
        });

    }

    private void setOnRefreshListener() {
        this.binding.swipeRefresh.setOnRefreshListener(this::reloadData);
    }

    private void startGuestActivity() {
        if (getActivity() != null) {
            Intent guestActivityIntent = new Intent(getActivity(), GuestActivity.class);
            startActivity(guestActivityIntent);
            getActivity().finish();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.viewModel = new ViewModelProvider(this).get(MainPageFragmentViewModel.class);
    }

    private void reloadData() {
        RecipesListModel.getInstance().refreshAllRecipes();
    }
}