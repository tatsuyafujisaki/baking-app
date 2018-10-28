package com.example.android.bakingapp.dagger;

import android.arch.lifecycle.ViewModel;

import dagger.MapKey;

@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
