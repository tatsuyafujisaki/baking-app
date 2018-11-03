package com.example.android.bakingapp.util.ui;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Objects;

public final class FragmentUtils {
    public static boolean hasArguments(Fragment fragment) {
        return fragment.getArguments() != null;
    }

    public static int getInt(Fragment fragment, String key) {
        return Objects.requireNonNull(fragment.getArguments()).getInt(key);
    }

    public static <T extends Parcelable> T getParcelable(Fragment fragment, String key) {
        return Objects.requireNonNull(fragment.getArguments()).getParcelable(key);
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(Fragment fragment, String key) {
        return Objects.requireNonNull(fragment.getArguments()).getParcelableArrayList(key);
    }

    // This works only with dynamic fragments
    public static void replace(FragmentManager fragmentManager, int frameLayoutId, Fragment fragment) {
        fragmentManager.beginTransaction().replace(frameLayoutId, fragment).commit();
    }
}