package com.example.android.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.RecipeRecyclerViewItemBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.example.android.bakingapp.util.ui.IntentBuilder;

import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {
    private final List<Recipe> recipes;

    public RecipeRecyclerViewAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecipeRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.binding.nameTextView.setText(recipe.name);
        holder.binding.servingsTextView.setText(holder.itemView.getContext().getString(R.string.servings_format, recipe.servings));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final RecipeRecyclerViewItemBinding binding;

        ViewHolder(RecipeRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            context.startActivity(new IntentBuilder(context, RecipeDetailActivity.class)
                                        .putParcelable(null, recipes.get(getAdapterPosition()))
                                        .build());
        }
    }
}