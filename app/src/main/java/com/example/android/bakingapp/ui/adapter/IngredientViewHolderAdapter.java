package com.example.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.*;
import com.example.android.bakingapp.room.Ingredient;

import java.util.List;

public class IngredientViewHolderAdapter extends RecyclerView.Adapter<IngredientViewHolderAdapter.ViewHolder> {
    private final List<Ingredient> ingredients;

    public IngredientViewHolderAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(IngredientViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        final IngredientViewHolderBinding binding;

        ViewHolder(IngredientViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}