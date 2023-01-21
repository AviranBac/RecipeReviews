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

import com.example.recipereviews.MainPageFragmentViewModal;
import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.databinding.FragmentMainPageBinding;
import com.example.recipereviews.models.Model;
import com.example.recipereviews.models.RecipeModel;
import com.example.recipereviews.models.entities.Recipe;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class MainPageFragment extends Fragment {

    private ShapeableImageView profileImageView;
    private ShapeableImageView logoutImageView;
    private EditText searchEditText;
    private RecipeRecyclerAdapter adapter;
    private MainPageFragmentViewModal viewModel;
    private FragmentMainPageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        this.initializeMembers(binding);
        View view = binding.getRoot();
        this.setListeners(view);
        this.loadData();
        return view;
    }

    private void loadData() {
        viewModel.getRecipeListData().observe(getViewLifecycleOwner(), list -> adapter.setData(list));
        RecipeModel.getInstance().EventRecipesListLoadingState.observe(getViewLifecycleOwner(), status -> binding.swipeRefresh.setRefreshing(status == RecipeModel.LoadingState.LOADING));
    }

    private void initializeMembers(FragmentMainPageBinding binding) {
        profileImageView = binding.profileImageView;
        logoutImageView = binding.logoutImageView;
        searchEditText = binding.searchEt;
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(), viewModel.getRecipeListData().getValue());
        binding.recyclerView.setAdapter(adapter);
    }

    private void setListeners(View view) {
        // TODO: need to set profile image from current user's imageUrl
        this.setProfileImageClickListener(view);
        this.setLogoutButtonClickListener();
        this.setSearchEditTextChangeListener();
        this.setOnRecipeClickListener();
        this.setOnRefreshListener();
    }

    private void setSearchEditTextChangeListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
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
        profileImageView.setOnClickListener((View view1) -> Navigation.findNavController(view).navigate(MainPageFragmentDirections.actionMainPageFragmentToProfileFragment(Model.getInstance().getCurrentUserId())));
    }

    private void setLogoutButtonClickListener() {
        logoutImageView.setOnClickListener(view -> Model.getInstance().logout(this::startGuestActivity));
    }

    private void setOnRecipeClickListener() {
        adapter.setOnItemClickListener(pos -> {
            Recipe recipe = Objects.requireNonNull(viewModel.getRecipeListData().getValue()).get(pos);
            // TODO: enter recipe fragment
        });

    }

    private void setOnRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener(this::reloadData);
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
        viewModel = new ViewModelProvider(this).get(MainPageFragmentViewModal.class);
    }

    void reloadData() {
        RecipeModel.getInstance().refreshAllRecipes();
    }
}