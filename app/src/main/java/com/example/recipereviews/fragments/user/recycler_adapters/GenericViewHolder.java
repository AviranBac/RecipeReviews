package com.example.recipereviews.fragments.user.recycler_adapters;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.function.Consumer;

public abstract class GenericViewHolder<T> extends RecyclerView.ViewHolder {

    protected final ViewBinding binding;

    public GenericViewHolder(ViewBinding binding, Consumer<Integer> itemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.initMembers();
        this.setListeners(itemClickListener);
    }

    protected void initMembers() {
    }

    protected void setListeners(Consumer<Integer> itemClickListener) {
        this.binding.getRoot().setOnClickListener(view ->
                itemClickListener.accept(getAdapterPosition())
        );
    }

    public abstract void bind(T object);
}
