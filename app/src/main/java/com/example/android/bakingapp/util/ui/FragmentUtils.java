package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

public final class FragmentUtils {
    private static final String KEY = null;

    public static <T extends Parcelable> T getArguments(Fragment fragment) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? (T) arguments.getParcelable(KEY) : null;
    }

    public static void setArguments(Fragment fragment, Parcelable parcelable) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY, parcelable);
        fragment.setArguments(arguments);
    }
}