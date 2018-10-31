package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class BundleUtils {
    private static final String KEY = null;

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(@NonNull Bundle bundle) {
        return bundle.getParcelableArrayList(KEY);
    }

    public static void putParcelableList(@NonNull Bundle bundle, List<? extends Parcelable> value) {
        if (value != null) {
            if (value instanceof ArrayList) {
                bundle.putParcelableArrayList(KEY, (ArrayList<? extends Parcelable>)value);
            } else {
                bundle.putParcelableArrayList(KEY, new ArrayList<>(value));
            }
        }
    }
}