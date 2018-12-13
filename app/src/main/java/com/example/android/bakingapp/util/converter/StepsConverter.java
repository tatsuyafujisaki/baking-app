package com.example.android.bakingapp.util.converter;

import com.example.android.bakingapp.room.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class StepsConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String toJson(@NonNull List<Step> Steps) {
        return gson.toJson(Steps, new TypeToken<List<Step>>() {
        }.getType());
    }

    @TypeConverter
    public static List<Step> toSteps(@NonNull String json) {
        return gson.fromJson(json, new TypeToken<List<Step>>() {
        }.getType());
    }
}