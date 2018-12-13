package com.example.android.bakingapp.ui.activity;

import android.os.Bundle;

import com.example.android.bakingapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
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