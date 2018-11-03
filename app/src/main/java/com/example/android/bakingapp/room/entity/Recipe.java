package com.example.android.bakingapp.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.bakingapp.room.Ingredient;
import com.example.android.bakingapp.room.Step;
import com.example.android.bakingapp.util.converter.IngredientsConverter;
import com.example.android.bakingapp.util.converter.StepsConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe implements Parcelable {
    @PrimaryKey
    public int id;
    public String name;
    public int servings;

    @TypeConverters(IngredientsConverter.class)
    public List<Ingredient> ingredients;

    @TypeConverters(StepsConverter.class)
    public List<Step> steps;

    public Recipe() {
    }

    private Recipe(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        servings = parcel.readInt();

        ingredients = new ArrayList<>();
        parcel.readTypedList(ingredients, Ingredient.CREATOR);

        steps = new ArrayList<>();
        parcel.readTypedList(steps, Step.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}