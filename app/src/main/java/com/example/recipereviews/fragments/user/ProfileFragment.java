package com.example.recipereviews.fragments.user;

import static com.example.recipereviews.fragments.user.ProfileFragmentDirections.actionProfileFragmentToReviewDetailsFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentProfileBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.fragments.user.recycler_adapters.ProfileReviewRecyclerAdapter;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.viewModels.ProfileFragmentViewModel;
import com.example.recipereviews.viewModels.factory.ProfileFragmentViewModelFactory;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private String userId;
    private FragmentProfileBinding binding;
    private ShapeableImageView profileImageView;
    private ProfileReviewRecyclerAdapter adapter;
    private ProfileFragmentViewModel viewModel;

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
        this.userId = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        this.viewModel = new ViewModelProvider(this, new ProfileFragmentViewModelFactory(this.userId)).get(ProfileFragmentViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reloadData();
    }

    private void initializeMembers() {
        this.profileImageView = this.binding.profileImageView;

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
            String reviewId = Objects.requireNonNull(this.viewModel.getReviewListDataByUserId().getValue()).get(pos).getReview().getId();
            Navigation.findNavController(view).navigate(actionProfileFragmentToReviewDetailsFragment(reviewId));
        });
    }

    private void setOnRefreshListener() {
        this.binding.swipeRefresh.setOnRefreshListener(this::reloadData);
    }

    private void reloadData() {
        ReviewModel.getInstance().refreshReviewByUserId(this.userId);
    }

    private void addObservers() {
        this.observeProfileImage();
        this.viewModel.getReviewListDataByUserId()
                .observe(getViewLifecycleOwner(), list -> this.adapter.setData(list));
        ReviewModel.getInstance().getProfileReviewListLoadingState()
                .observe(getViewLifecycleOwner(), status -> this.binding.swipeRefresh.setRefreshing(status == LoadingState.LOADING));
    }

    private void observeProfileImage() {
        this.viewModel.getProfileUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                ImageUtil.loadImage(this.profileImageView, user.getImageUrl(), R.drawable.blank_profile_picture);
            }
        });
    }
}