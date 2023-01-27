package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipereviews.R;
import com.example.recipereviews.databinding.FragmentReviewListBinding;
import com.example.recipereviews.fragments.user.recycler_adapters.ReviewRecyclerAdapter;
import com.example.recipereviews.models.models.ReviewListModel;
import com.example.recipereviews.viewModels.ReviewListFragmentViewModel;
import com.example.recipereviews.viewModels.factory.ReviewListFragmentViewModelFactory;

public class ReviewListFragment extends Fragment {
    private static final String RECIPE_ID_PARAM = "recipe_id";
    private FragmentReviewListBinding binding;
    private ReviewRecyclerAdapter adapter;
    private ReviewListFragmentViewModel viewModel;
    private TextView reviewsSectionTextView;
    private int recipeId;

    public static ReviewListFragment newInstance(int recipeId) {
        ReviewListFragment frag = new ReviewListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID_PARAM, recipeId);
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.recipeId = bundle.getInt(RECIPE_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentReviewListBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();
        this.initMembers();
        this.setListeners();
        this.loadData();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.recipeId = getArguments().getInt(RECIPE_ID_PARAM);
        this.viewModel = new ViewModelProvider(this, new ReviewListFragmentViewModelFactory(this.recipeId)).get(ReviewListFragmentViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        ReviewListModel.getInstance().refreshReviewByRecipeId(this.recipeId);
    }

    private void initMembers() {
        this.reviewsSectionTextView = this.binding.reviewsSectionTextView;

        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new ReviewRecyclerAdapter(getLayoutInflater(), this.viewModel.getReviewListDataByRecipeId().getValue());
        this.binding.recyclerView.setAdapter(this.adapter);
    }

    private void setListeners() {
        this.adapter.setOnItemClickListener(pos -> {
            // TODO: navigate to review fragment
        });
    }

    private void loadData() {
        viewModel.getReviewListDataByRecipeId().observe(getViewLifecycleOwner(), list -> {
            this.adapter.setData(list);
            this.reviewsSectionTextView.setText(requireContext().getString(R.string.reviews_header, String.valueOf(list.size())));
        });
    }
}