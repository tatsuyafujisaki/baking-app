package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public final class FragmentUtils {
    private static final String KEY = null;

    public static <T extends Parcelable> T getArguments(Fragment fragment) {
        Bundle arguments = fragment.getArguments();
        return arguments != null ? arguments.getParcelable(KEY) : null;
    }

    public static void setArguments(Fragment fragment, Parcelable parcelable) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY, parcelable);
        fragment.setArguments(arguments);
    }

    // This does not work with static fragments
    public static void replace(FragmentManager fragmentManager, int frameLayoutId, Fragment fragment) {
        fragmentManager.beginTransaction().replace(frameLayoutId, fragment).commit();
    }
}