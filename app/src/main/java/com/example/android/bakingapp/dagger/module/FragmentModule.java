package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.ui.fragment.DetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();
}
