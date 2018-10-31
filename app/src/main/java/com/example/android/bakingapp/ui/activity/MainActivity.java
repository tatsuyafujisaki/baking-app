package com.example.android.bakingapp.ui.activity;

import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityMainBinding;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.adapter.RecipeAdapter;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.NetworkUtils;
import com.example.android.bakingapp.util.ui.BundleUtils;
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
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_unavailable_error));
            return;
        }

        if (savedInstanceState != null) {
            recipes = BundleUtils.getParcelableArrayList(savedInstanceState);
            binding.include.recipeRecyclerView.setAdapter(new RecipeAdapter(getSupportFragmentManager(), recipes));
            return;
        }

        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if(response.isSuccessful) {
            response.data.observe(this, recipes -> {
                /*
                 * This observer is called twice.
                 * For the first time, child_activity_main.xml.xml is null because downloading child_activity_main.xml.xml in a different thread has not completed.
                 * For the second time, child_activity_main.xml.xml is not null because downloading child_activity_main.xml.xml in a different thread has completed.
                 */
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    this.recipes = recipes;
                    binding.include.recipeRecyclerView.setAdapter(new RecipeAdapter(getSupportFragmentManager(), recipes));
                    response.data.removeObservers(this);
                }
            });
        } else {
            showToast(response.errorMessage);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        BundleUtils.putParcelableList(outState, recipes);
        super.onSaveInstanceState(outState);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}