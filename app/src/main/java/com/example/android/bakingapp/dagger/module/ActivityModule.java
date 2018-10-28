package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.ui.activity.DetailActivity;
import com.example.android.bakingapp.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();
}