package com.example.android.bakingapp.ui.activity;

import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityMainBinding;
import com.example.android.bakingapp.dummy.DummyContent;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.adapter.RecyclerViewAdapter;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    @Inject
    RecipeViewModel recipeViewModel;

    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());
        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        boolean isTwoPane = getResources().getBoolean(R.bool.is_tablet);

        binding.include.stepRecyclerView.setAdapter(new RecyclerViewAdapter(getSupportFragmentManager(), DummyContent.ITEMS, isTwoPane));

        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if(response.isSuccessful) {
            response.data.observe(this, recipes -> {
                /*
                 * This observer is called twice.
                 * For the first time, recipes is null because downloading recipes has not completed.
                 * For the second time, recipes is not null because downloading recipes in a different thread has completed.
                 */
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    this.recipes = recipes;

                    //binding.recyclerView.setAdapter(new MovieAdapter(this, movies));
                    response.data.removeObservers(this);
                }
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}