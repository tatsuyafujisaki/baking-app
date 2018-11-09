package com.example.android.bakingapp.util.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class IntentUtils {
    public static int getInt(Intent intent, String key) {
        return intent.getIntExtra(key, Integer.MIN_VALUE);
    }

    public static int getInt(Fragment fragment, String key) {
        return getInt(getIntent(fragment), key);
    }

    public static int[] getIntArray(Intent intent, String key) {
        return intent.getIntArrayExtra(key);
    }

    public static String getString(Fragment fragment, String key) {
        return getIntent(fragment).getStringExtra(key);
    }

    public static <T extends Parcelable> T getParcelable(Fragment fragment, String key) {
        return getIntent(fragment).getParcelableExtra(key);
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayList(Fragment fragment, String key) {
        return getIntent(fragment).getParcelableArrayListExtra(key);
    }

    private static Intent getIntent(Fragment fragment) {
        return fragment.requireActivity().getIntent();
    }
}