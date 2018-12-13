package com.example.android.bakingapp.room.dao;

import com.example.android.bakingapp.room.entity.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> load();

    @Insert(onConflict = REPLACE)
    void save(List<Recipe> recipes);
}