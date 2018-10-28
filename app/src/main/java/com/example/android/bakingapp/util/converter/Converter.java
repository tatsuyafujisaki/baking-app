package com.example.android.bakingapp.util.converter;

import java.util.ArrayList;
import java.util.Arrays;

public class Converter {
    public static <T> ArrayList<T> toArrayList(T[] xs) {
        return new ArrayList<>(Arrays.asList(xs));
    }
}