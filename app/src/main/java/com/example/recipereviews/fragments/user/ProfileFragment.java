package com.example.recipereviews.fragments.user;

import static com.example.recipereviews.fragments.user.ProfileFragmentDirections.actionProfileFragmentToReviewDetailsFragment;

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
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentProfileBinding;
import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.fragments.user.recycler_adapters.ProfileReviewRecyclerAdapter;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;
import com.example.recipereviews.utils.ImageUtil;
import com.example.recipereviews.utils.NavigationUtils;
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
    private SwipeRefreshLayout swipeRefresh;
    private TextView fullNameTv;
    private TextView emailTv;

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
        this.userId = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        this.viewModel = new ViewModelProvider(this, new ProfileFragmentViewModelFactory(this.userId)).get(ProfileFragmentViewModel.class);
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
                    NavigationUtils.navigate(parentActivity, ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(userId));
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
            String reviewId = Objects.requireNonNull(this.viewModel.getReviewListDataByUserId().getValue()).get(pos).getReview().getId();
            NavigationUtils.navigate(view, actionProfileFragmentToReviewDetailsFragment(reviewId));
        });
    }

    private void setOnRefreshListener() {
        this.swipeRefresh.setOnRefreshListener(this::reloadData);
    }

    private void reloadData() {
        ReviewModel.getInstance().refreshReviewByUserId(this.userId);
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
                ImageUtil.loadImage(this.profileImageView, user.getImageUrl(), R.drawable.blank_profile_picture);
            }
        });
    }

    private void observeRefresh() {
        MediatorLiveData<LoadingState> refreshMerger = new MediatorLiveData<>();
        refreshMerger.addSource(ReviewModel.getInstance().getProfileReviewListLoadingState(), value -> {
            LoadingState loadingState = UserModel.getInstance().getUserLoadingState().getValue() == LoadingState.NOT_LOADING && value == LoadingState.NOT_LOADING ?
                    LoadingState.NOT_LOADING :
                    LoadingState.LOADING;
            refreshMerger.setValue(loadingState);
        });
        refreshMerger.addSource(UserModel.getInstance().getUserLoadingState(), value -> {
            LoadingState loadingState = ReviewModel.getInstance().getProfileReviewListLoadingState().getValue() == LoadingState.NOT_LOADING && value == LoadingState.NOT_LOADING ?
                    LoadingState.NOT_LOADING :
                    LoadingState.LOADING;
            refreshMerger.setValue(loadingState);
        });
        refreshMerger.observe(getViewLifecycleOwner(), (LoadingState status) -> this.swipeRefresh.setRefreshing(status == LoadingState.LOADING));
    }
}