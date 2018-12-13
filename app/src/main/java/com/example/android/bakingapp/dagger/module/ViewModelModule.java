package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.dagger.ViewModelFactory;
import com.example.android.bakingapp.dagger.ViewModelKey;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);
}
