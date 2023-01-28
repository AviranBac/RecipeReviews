package com.example.recipereviews.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recipereviews.databinding.FragmentReviewDetailsBinding;

public class ReviewDetailsFragment extends Fragment {

    private FragmentReviewDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentReviewDetailsBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();

        return view;
    }
}