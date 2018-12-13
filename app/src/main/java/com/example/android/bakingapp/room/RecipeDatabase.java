package com.example.android.bakingapp.room;

import android.content.Context;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.dao.RecipeDao;
import com.example.android.bakingapp.room.entity.Recipe;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase instance;

    public static RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (RecipeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, context.getString(R.string.database_name)).build();
                }
            }
        }

        return instance;
    }

    public abstract RecipeDao recipeDao();
}