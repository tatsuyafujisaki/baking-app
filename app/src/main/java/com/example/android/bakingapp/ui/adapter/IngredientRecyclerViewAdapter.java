package com.example.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.IngredientRecyclerViewItemBinding;
import com.example.android.bakingapp.room.Ingredient;

import java.util.List;

public final class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {
    private final List<Ingredient> ingredients;

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(IngredientRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.binding.ingredientTextView.setText(ingredient.ingredient);
        holder.binding.measureTextView.setText(ingredient.measure);
        holder.binding.quantityTextView.setText(String.valueOf(ingredient.quantity));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final IngredientRecyclerViewItemBinding binding;

        ViewHolder(IngredientRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}