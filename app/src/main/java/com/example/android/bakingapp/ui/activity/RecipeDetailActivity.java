package com.example.android.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;

import dagger.android.AndroidInjection;

public class RecipeDetailActivity extends AppCompatActivity {
    @Nullable
    private CountingIdlingResource countingIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }

    @VisibleForTesting
    @NonNull
    IdlingResource getIdlingResource() {
        return countingIdlingResource != null
                ? countingIdlingResource
                : new CountingIdlingResource("MainActivityCountingIdlingResource");
    }
}