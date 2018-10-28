package com.example.android.bakingapp.util.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.room.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class IngredientsConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String toJson(@NonNull List<Ingredient> ingredients) {
        return gson.toJson(ingredients, new TypeToken<List<Ingredient>>() {}.getType());
    }

    @TypeConverter
    public static List<Ingredient> toIngredients(@NonNull String json) {
        return gson.fromJson(json, new TypeToken<List<Ingredient>>() {}.getType());
    }
}