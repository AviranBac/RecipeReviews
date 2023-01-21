package com.example.recipereviews.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.databinding.FragmentMainPageBinding;
import com.example.recipereviews.models.Model;
import com.google.android.material.imageview.ShapeableImageView;

public class MainPageFragment extends Fragment {

    private ShapeableImageView profileImageView;
    private ShapeableImageView logoutImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainPageBinding binding = FragmentMainPageBinding.inflate(inflater, container, false);
        this.initializeMembers(binding);

        View view = binding.getRoot();
        this.setListeners(view);

        return view;
    }

    private void initializeMembers(FragmentMainPageBinding binding) {
        profileImageView = binding.profileImageView;
        logoutImageView = binding.logoutImageView;
    }

    private void setListeners(View view) {
        // TODO: need to set profile image from current user's imageUrl
        this.setProfileImageClickListener(view);
        this.setLogoutButtonClickListener();
    }

    private void setProfileImageClickListener(View view) {
        profileImageView.setOnClickListener((View view1) -> {
            Navigation.findNavController(view).navigate(MainPageFragmentDirections.actionMainPageFragmentToProfileFragment(Model.getInstance().getCurrentUserId()));
        });
    }

    private void setLogoutButtonClickListener() {
        logoutImageView.setOnClickListener(view -> {
            Model.getInstance().logout(this::startGuestActivity);
        });
    }

    private void startGuestActivity() {
        if (getActivity() != null) {
            Intent guestActivityIntent = new Intent(getActivity(), GuestActivity.class);
            startActivity(guestActivityIntent);
            getActivity().finish();
        }
    }
}