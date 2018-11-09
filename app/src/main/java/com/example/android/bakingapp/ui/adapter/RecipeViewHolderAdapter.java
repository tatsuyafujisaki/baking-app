package com.example.android.bakingapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.RecipeViewHolderBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;

import java.util.List;

import static com.example.android.bakingapp.ui.fragment.RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;

public class RecipeViewHolderAdapter extends RecyclerView.Adapter<RecipeViewHolderAdapter.ViewHolder> {
    private final List<Recipe> recipes;

    public RecipeViewHolderAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecipeViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.binding.recipeTextView.setText(recipe.name);
        holder.binding.servingsTextView.setText(holder.itemView.getContext().getString(R.string.servings_format, recipe.servings));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final RecipeViewHolderBinding binding;

        ViewHolder(RecipeViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            context.startActivity(new Intent(context, RecipeDetailActivity.class)
                    .putExtra(RECIPE_PARCELABLE_EXTRA_KEY, recipes.get(getAdapterPosition())));
        }
    }
}