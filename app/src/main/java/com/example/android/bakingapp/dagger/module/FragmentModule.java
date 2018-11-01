package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.ui.fragment.RecipeDetailFragment;
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract RecipeDetailFragment contributeRecipeDetailFragment();

    @ContributesAndroidInjector
    abstract StepDetailFragment contributeStepDetailFragment();
}