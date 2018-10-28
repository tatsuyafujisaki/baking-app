package com.example.android.bakingapp.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.room.entity.Recipe;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> load();

    @Insert(onConflict = REPLACE)
    void save(List<Recipe> recipes);
}