package com.example.recipereviews.fragments.user;

import static com.example.recipereviews.fragments.user.ProfileFragmentDirections.actionGlobalReviewDetailsFragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentProfileBinding;
import com.example.recipereviews.fragments.user.recycler_adapters.ProfileReviewRecyclerAdapter;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtils;
import com.example.recipereviews.utils.LiveDataUtils;
import com.example.recipereviews.utils.NavigationUtils;
import com.example.recipereviews.viewModels.ProfileFragmentViewModel;
import com.example.recipereviews.viewModels.SharedViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ShapeableImageView profileImageView;
    private ProfileReviewRecyclerAdapter adapter;
    private ProfileFragmentViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private TextView fullNameTv;
    private TextView emailTv;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();

        this.initializeMembers();
        this.setListeners(view);
        this.addObservers();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
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
                menuInflater.inflate(R.menu.profile_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.edit_profile) {
                    NavigationUtils.navigate(parentActivity, ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment());
                }

                return false;
            }
        },this, Lifecycle.State.RESUMED);
    }

    private void initializeMembers() {
        this.profileImageView = this.binding.profileImageView;
        this.swipeRefresh = this.binding.swipeRefresh;
        this.fullNameTv = this.binding.fullNameTv;
        this.emailTv = this.binding.emailTv;
        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new ProfileReviewRecyclerAdapter(getLayoutInflater(), this.viewModel.getReviewListDataByUserId().getValue());
        this.binding.recyclerView.setAdapter(this.adapter);
    }

    private void setListeners(View view) {
        this.setOnReviewClickListener(view);
        this.setOnRefreshListener();
    }

    private void setOnReviewClickListener(View view) {
        this.adapter.setOnItemClickListener(pos -> {
            this.sharedViewModel.setReviewData(Objects.requireNonNull(this.viewModel.getReviewListDataByUserId().getValue()).get(pos).getReview());
            this.sharedViewModel.setRecipeData(Objects.requireNonNull(this.viewModel.getReviewListDataByUserId().getValue()).get(pos).getRecipe());
            this.sharedViewModel.setUserData(Objects.requireNonNull(this.viewModel.getProfileUser().getValue()));
            NavigationUtils.navigate(view, actionGlobalReviewDetailsFragment());
        });
    }

    private void setOnRefreshListener() {
        this.swipeRefresh.setOnRefreshListener(this::reloadData);
    }

    private void reloadData() {
        ReviewModel.getInstance().refreshLoggedInUserReviews();
    }

    private void addObservers() {
        this.observeUser();
        this.observeRefresh();
        this.viewModel.getReviewListDataByUserId()
                .observe(getViewLifecycleOwner(), list -> this.adapter.setData(list));
    }

    private void observeUser() {
        this.viewModel.getProfileUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.fullNameTv.setText(user.getFullName());
                this.emailTv.setText(user.getEmail());
                this.emailTv.setPaintFlags(this.emailTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ImageUtils.loadImage(this.profileImageView, user.getImageUrl(), R.drawable.blank_profile_picture);
            }
        });
    }

    private void observeRefresh() {
        LiveDataUtils.observeRefreshMerger(
                getViewLifecycleOwner(),
                ReviewModel.getInstance().getProfileReviewListLoadingState(),
                UserModel.getInstance().getLoggedInUserLoadingState(),
                this.swipeRefresh
        );
    }
}