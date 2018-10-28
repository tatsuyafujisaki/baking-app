package com.example.android.bakingapp.util;

import com.example.android.bakingapp.room.entity.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("android-baking-app-json")
    Call<Recipe[]> getRecipes();
}