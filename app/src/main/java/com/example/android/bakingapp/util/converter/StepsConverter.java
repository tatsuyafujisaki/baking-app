package com.example.android.bakingapp.util.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.room.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class StepsConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String toJson(@NonNull List<Step> Steps) {
        return gson.toJson(Steps, new TypeToken<List<Step>>() {}.getType());
    }

    @TypeConverter
    public static List<Step> toSteps(@NonNull String json) {
        return gson.fromJson(json, new TypeToken<List<Step>>() {}.getType());
    }
}