package com.example.recipereviews.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recipereviews.databinding.FragmentMainPageBinding;

public class MainPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainPageBinding binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}