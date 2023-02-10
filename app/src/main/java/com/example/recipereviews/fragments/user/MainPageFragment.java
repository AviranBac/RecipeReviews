package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipereviews.R;
import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.databinding.FragmentMainPageBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.fragments.user.recycler_adapters.RecipeRecyclerAdapter;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.RecipeModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.viewModels.MainPageFragmentViewModel;

import java.util.Objects;

public class MainPageFragment extends Fragment {

    private EditText searchEditText;
    private RecipeRecyclerAdapter adapter;
    private MainPageFragmentViewModel viewModel;
    private FragmentMainPageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initializeMembers();
        this.setListeners(view);
        this.addObservers();
        this.reloadData();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.viewModel = new ViewModelProvider(this).get(MainPageFragmentViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reloadData();
    }

    private void initializeMenu() {
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_page_menu, menu);
                observeProfileImage(menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.profile) {
                    NavigationUtils.navigate(parentActivity, MainPageFragmentDirections.actionMainPageFragmentToProfileFragment());
                } else if (menuItem.getItemId() == R.id.logout) {
                    UserModel.getInstance().logout(() -> {
                        UserModel.getInstance().getLoggedInUser().setValue(null);
                        startGuestActivity();
                    });
                }

                return false;
            }
        },this, Lifecycle.State.RESUMED);
    }

    private void initializeMembers() {
        this.searchEditText = this.binding.searchEt;

        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new RecipeRecyclerAdapter(getLayoutInflater(), this.viewModel.getSearchedRecipes().getValue());
        this.binding.recyclerView.setAdapter(this.adapter);
    }

    private void setListeners(View view) {
        this.setSearchEditTextChangeListener();
        this.setOnRecipeClickListener(view);
        this.setOnRefreshListener();
    }

    private void setSearchEditTextChangeListener() {
        this.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String text = searchEditText.getText().toString();
                viewModel.setNameQuery(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setOnRecipeClickListener(View view) {
        this.adapter.setOnItemClickListener(pos -> {
            Recipe recipe = Objects.requireNonNull(this.viewModel.getSearchedRecipes().getValue()).get(pos);
            NavigationUtils.navigate(view, MainPageFragmentDirections.actionMainPageFragmentToRecipeDetailsFragment(recipe.getId()));
        });
    }

    private void setOnRefreshListener() {
        this.binding.swipeRefresh.setOnRefreshListener(this::reloadData);
    }

    private void addObservers() {
        this.viewModel.getSearchedRecipes()
                .observe(getViewLifecycleOwner(), list -> adapter.setData(list));
        RecipeModel.getInstance().getRecipeListLoadingState()
                .observe(getViewLifecycleOwner(), status -> this.binding.swipeRefresh.setRefreshing(status == LoadingState.LOADING));
    }

    private void observeProfileImage(Menu mainPageMenu) {
        this.viewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            MenuItem profileMenuItem = mainPageMenu.findItem(R.id.profile);

            if (user != null) {
                ImageUtil.loadImage(profileMenuItem, user.getImageUrl(), R.drawable.blank_profile_picture);
            } else {
                profileMenuItem.setIcon(0);
            }
        });
    }

    private void startGuestActivity() {
        if (getActivity() != null) {
            Intent guestActivityIntent = new Intent(getActivity(), GuestActivity.class);
            startActivity(guestActivityIntent);
            getActivity().finish();
        }
    }

    private void reloadData() {
        UserModel.getInstance().refreshLoggedInUser(() -> {});
        RecipeModel.getInstance().refreshAllRecipes();
    }
}