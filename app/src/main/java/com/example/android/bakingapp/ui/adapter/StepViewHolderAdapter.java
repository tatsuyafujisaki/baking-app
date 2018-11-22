package com.example.android.bakingapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.StepViewHolderBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.StepDetailActivity;
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;
import com.example.android.bakingapp.util.ui.FragmentBuilder;
import com.example.android.bakingapp.util.ui.FragmentUtils;

import static com.example.android.bakingapp.ui.fragment.StepDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;
import static com.example.android.bakingapp.ui.fragment.StepDetailFragment.STEP_INDEX_INT_EXTRA_KEY;

public class StepViewHolderAdapter extends RecyclerView.Adapter<StepViewHolderAdapter.ViewHolder> {
    private final Context context;
    private final FragmentManager fragmentManager;
    private final Recipe recipe;
    private final boolean isTablet;

    public StepViewHolderAdapter(Context context, FragmentManager fragmentManager, Recipe recipe, boolean isTablet) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.recipe = recipe;
        this.isTablet = isTablet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StepViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.stepTextView.setText(context.getString(R.string.step_format, position + 1, recipe.steps.get(position).shortDescription));
    }

    @Override
    public int getItemCount() {
        return recipe.steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final StepViewHolderBinding binding;

        ViewHolder(StepViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (isTablet) {
                    Fragment fragment = new FragmentBuilder(new StepDetailFragment())
                            .putParcelable(RECIPE_PARCELABLE_EXTRA_KEY, recipe)
                            .putInt(STEP_INDEX_INT_EXTRA_KEY, getAdapterPosition())
                            .build();

                    FragmentUtils.replace(fragmentManager, R.id.step_detail_fragment_container, fragment);
                } else {
                    Intent intent = new Intent(context, StepDetailActivity.class)
                            .putExtra(RECIPE_PARCELABLE_EXTRA_KEY, recipe)
                            .putExtra(STEP_INDEX_INT_EXTRA_KEY, getAdapterPosition());

                    context.startActivity(intent);
                }
            });
        }
    }
}