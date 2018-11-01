package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.example.android.bakingapp.ui.activity.RecipeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract RecipeActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract RecipeDetailActivity contributeDetailActivity();
}