package com.example.recipereviews.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipereviews.enums.LoadingState;

public class LiveDataUtils {

    public static <T, K> void observeRefreshMerger(LifecycleOwner lifecycleOwner,
                                                   LiveData<LoadingState> firstSource,
                                                   LiveData<LoadingState> secondSource,
                                                   SwipeRefreshLayout swipeRefresh) {
        MediatorLiveData<LoadingState> refreshMerger = new MediatorLiveData<>();
        refreshMerger.addSource(firstSource, value -> {
            LoadingState loadingState = secondSource.getValue() == LoadingState.NOT_LOADING && value == LoadingState.NOT_LOADING ?
                    LoadingState.NOT_LOADING :
                    LoadingState.LOADING;
            refreshMerger.setValue(loadingState);
        });
        refreshMerger.addSource(secondSource, value -> {
            LoadingState loadingState = firstSource.getValue() == LoadingState.NOT_LOADING && value == LoadingState.NOT_LOADING ?
                    LoadingState.NOT_LOADING :
                    LoadingState.LOADING;
            refreshMerger.setValue(loadingState);
        });
        refreshMerger.observe(lifecycleOwner, (LoadingState status) -> swipeRefresh.setRefreshing(status == LoadingState.LOADING));
    }
}
