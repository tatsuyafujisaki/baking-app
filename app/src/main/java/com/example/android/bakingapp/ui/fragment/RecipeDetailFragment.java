package com.example.android.bakingapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.example.android.bakingapp.databinding.IngredientViewHolderBinding;
import com.example.android.bakingapp.databinding.StepViewHolderBinding;
import com.example.android.bakingapp.room.Ingredient;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.StepDetailActivity;
import com.example.android.bakingapp.util.ui.FragmentBuilder;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.android.bakingapp.ui.fragment.StepDetailFragment.STEP_INDEX_INT_EXTRA_KEY;

public class RecipeDetailFragment extends Fragment {
    public static final String RECIPE_PARCELABLE_EXTRA_KEY = "RECIPE_PARCELABLE_EXTRA_KEY";
    private FragmentRecipeDetailBinding binding;
    @Inject
    boolean isTablet;
    private Context context;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Recipe recipe = IntentUtils.getParcelable(this, RECIPE_PARCELABLE_EXTRA_KEY);

        requireActivity().setTitle(recipe.name);

        binding.ingredientRecyclerView.setAdapter(new IngredientAdapter(recipe.ingredients));
        binding.stepRecyclerView.setAdapter(new StepAdapter(context, requireFragmentManager(), recipe, isTablet));
    }

    private class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
        private final List<Ingredient> ingredients;

        private IngredientAdapter(List<Ingredient> ingredients) {
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

    private class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
        private final Context context;
        private final FragmentManager fragmentManager;
        private final Recipe recipe;
        private final boolean isTablet;

        private StepAdapter(Context context, FragmentManager fragmentManager, Recipe recipe, boolean isTablet) {
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
}