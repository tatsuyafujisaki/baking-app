package com.example.android.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.RecipeViewHolderBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.DetailActivity;
import com.example.android.bakingapp.ui.fragment.DetailFragment;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentUtils;

import java.util.List;

public final class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<Recipe> recipes;

    public RecipeAdapter(FragmentManager fragmentManager, List<Recipe> recipes) {
        this.recipes = recipes;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecipeViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.binding.nameTextView.setText(recipe.name);
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

            Recipe recipe = recipes.get(getAdapterPosition());

            if (context.getResources().getBoolean(R.bool.is_tablet)) {
                DetailFragment detailFragment = new DetailFragment();
                FragmentUtils.setArguments(detailFragment, recipe);
                fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, detailFragment).commit();
            } else {
                context.startActivity(IntentUtils.createIntent(context, DetailActivity.class, recipe));
            }
        }
    }
}