package com.example.android.bakingapp.room;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    public final double quantity;
    public final String measure;
    public final String ingredient;

    private Ingredient(Parcel parcel) {
        quantity = parcel.readDouble();
        measure = parcel.readString();
        ingredient = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}