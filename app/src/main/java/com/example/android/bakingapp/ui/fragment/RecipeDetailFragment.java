package com.example.android.bakingapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.adapter.IngredientViewHolderAdapter;
import com.example.android.bakingapp.ui.adapter.StepViewHolderAdapter;
import com.example.android.bakingapp.util.ui.IntentUtils;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RecipeDetailFragment extends Fragment {
    public static final String RECIPE_PARCELABLE_EXTRA_KEY = "STEP_INDEX";
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
        FragmentRecipeDetailBinding binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);

        Recipe recipe = IntentUtils.getParcelable(this, RECIPE_PARCELABLE_EXTRA_KEY);

        requireActivity().setTitle(recipe.name);

        binding.ingredientRecyclerView.setAdapter(new IngredientViewHolderAdapter(recipe.ingredients));
        binding.stepRecyclerView.setAdapter(new StepViewHolderAdapter(context, requireFragmentManager(), recipe, isTablet));

        return binding.getRoot();
    }
}