package com.example.android.bakingapp.ui.activity;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityRecipeBinding;
import com.example.android.bakingapp.databinding.RecipeViewHolderBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.NetworkUtils;
import com.example.android.bakingapp.util.converter.Converter;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.example.android.bakingapp.ui.fragment.RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;

public class RecipeActivity extends AppCompatActivity {
    @Inject
    boolean isTablet;

    @Inject
    int gridColumnSpan;

    @Inject
    RecipeViewModel recipeViewModel;

    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        ActivityRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_unavailable_error));
            return;
        }

        if (isTablet) {
            ((GridLayoutManager) Objects.requireNonNull(binding.recipeRecyclerView.getLayoutManager())).setSpanCount(gridColumnSpan);
        }

        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(null);
            setAdapter(binding);
            return;
        }

        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if (response.isSuccessful) {
            response.data.observe(this, recipes -> {
                /*
                 * This observer is called twice.
                 * For the first time, child_activity_main.xml.xml is null because downloading child_activity_main.xml.xml in a different thread has not completed.
                 * For the second time, child_activity_main.xml.xml is not null because downloading child_activity_main.xml.xml in a different thread has completed.
                 */
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    this.recipes = recipes;
                    setAdapter(binding);
                    response.data.removeObservers(this);
                }
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(null, Converter.toArrayList(recipes));
        super.onSaveInstanceState(outState);
    }

    private void setAdapter(ActivityRecipeBinding binding) {
        binding.recipeRecyclerView.setAdapter(new RecipeViewHolderAdapter(recipes));
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private class RecipeViewHolderAdapter extends RecyclerView.Adapter<RecipeViewHolderAdapter.ViewHolder> {
        private final List<Recipe> recipes;

        private RecipeViewHolderAdapter(List<Recipe> recipes) {
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final RecipeViewHolderBinding binding;

            ViewHolder(RecipeViewHolderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnClickListener(v -> {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, RecipeDetailActivity.class)
                            .putExtra(RECIPE_PARCELABLE_EXTRA_KEY, recipes.get(getAdapterPosition())));
                });
            }
        }
    }
}