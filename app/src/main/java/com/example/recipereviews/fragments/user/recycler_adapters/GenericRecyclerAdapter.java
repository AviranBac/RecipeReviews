package com.example.recipereviews.fragments.user.recycler_adapters;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public abstract class GenericRecyclerAdapter<T, K extends GenericViewHolder<T>> extends RecyclerView.Adapter<K> {

    protected Consumer<Integer> itemClickListener;
    protected LayoutInflater inflater;
    protected List<T> data;

    public GenericRecyclerAdapter(LayoutInflater inflater, List<T> data) {
        this.inflater = inflater;
        this.data = data;
    }

    public void setOnItemClickListener(Consumer<Integer> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull K holder, int position) {
        holder.bind(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return this.data == null ? 0 : this.data.size();
    }
}