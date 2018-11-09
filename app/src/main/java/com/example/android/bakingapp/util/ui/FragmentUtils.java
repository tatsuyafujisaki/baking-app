package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentUtils {
    public static int getInt(Fragment fragment, String key) {
        return requireArguments(fragment).getInt(key);
    }

    public static <T extends Parcelable> T getParcelable(Fragment fragment, String key) {
        return requireArguments(fragment).getParcelable(key);
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(Fragment fragment, String key) {
        return requireArguments(fragment).getParcelableArrayList(key);
    }

    // This works only with dynamic fragments
    public static void replace(FragmentManager fragmentManager, int frameLayoutId, Fragment fragment) {
        fragmentManager.beginTransaction().replace(frameLayoutId, fragment).commit();
    }

    private static Bundle requireArguments(Fragment fragment) {
        return Objects.requireNonNull(fragment.getArguments());
    }
}