package com.example.recipereviews;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipereviews.databinding.FragmentMainPageBinding;
import com.example.recipereviews.model.Recipe;

public class MainPageFragment extends Fragment {
    FragmentMainPageBinding binding;
    RecipeRecyclerAdapter adapter;
    MainPageFragmentViewModal viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Recipe recipe = viewModel.getData().get(pos);
                // TODO: add navigation
            }
        });

        // TODO: replace mock data with live data
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MainPageFragmentViewModal.class);
    }

}

