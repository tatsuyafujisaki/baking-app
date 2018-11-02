package com.example.android.bakingapp.room.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.room.dao.RecipeDao;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.MyDateUtils;
import com.example.android.bakingapp.util.RecipeService;
import com.example.android.bakingapp.util.converter.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private final RecipeService recipeService;
    private final RecipeDao recipeDao;
    private final Executor executor;
    private String errorMessage;
    private long lastUpdate;

    public RecipeRepository(RecipeService recipeService, RecipeDao recipeDao, Executor executor) {
        this.recipeService = recipeService;
        this.recipeDao = recipeDao;
        this.executor = executor;
    }

    public ApiResponse<LiveData<List<Recipe>>> getRecipes() {
        errorMessage = null;

        if (hasExpired()) {
            recipeService.getRecipes().enqueue(new Callback<Recipe[]>() {
                @Override
                public void onResponse(@NonNull Call<Recipe[]> call, @NonNull Response<Recipe[]> response) {
                    if (response.isSuccessful()) {
                        List<Recipe> recipes = Converter.toArrayList(response.body());

                        executor.execute(() -> {
                            recipeDao.save(recipes);
                            lastUpdate = System.currentTimeMillis();
                        });
                    } else {
                        try {
                            errorMessage = Objects.requireNonNull(response.errorBody()).string();
                        } catch (IOException e) {
                            errorMessage = e.getMessage();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Recipe[]> call, @NonNull Throwable t) {
                    errorMessage = t.getMessage();
                    t.printStackTrace();
                }
            });
        }

        return errorMessage == null ? ApiResponse.success(recipeDao.load()) : ApiResponse.failure(errorMessage);
    }

    private boolean hasExpired() {
        int daysToExpire = 1;
        return MyDateUtils.Day.hasExpired(lastUpdate, daysToExpire);
    }
}