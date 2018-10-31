package com.example.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.IngredientViewHolderBinding;
import com.example.android.bakingapp.room.Ingredient;

import java.util.List;

public final class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(IngredientViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        final IngredientViewHolderBinding binding;

        ViewHolder(IngredientViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}