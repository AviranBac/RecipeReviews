package com.example.recipereviews.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.recipereviews.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    private String userId;
    private FragmentEditProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = this.binding.getRoot();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.userId = EditProfileFragmentArgs.fromBundle(getArguments()).getUserId();
    }
}