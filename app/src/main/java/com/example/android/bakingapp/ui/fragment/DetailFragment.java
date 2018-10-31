package com.example.android.bakingapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentDetailBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.adapter.IngredientAdapter;
import com.example.android.bakingapp.ui.adapter.StepAdapter;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentUtils;

import dagger.android.support.AndroidSupportInjection;

public class DetailFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);

        Recipe recipe = getResources().getBoolean(R.bool.is_tablet)
                        ? FragmentUtils.getArguments(this)
                        : IntentUtils.getParcelableExtra(this);

        binding.ingredientRecyclerView.setAdapter(new IngredientAdapter(recipe.ingredients));
        binding.stepRecyclerView.setAdapter(new StepAdapter(recipe.steps));

        return binding.getRoot();
    }
}