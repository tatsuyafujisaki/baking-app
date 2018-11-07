package com.example.android.bakingapp.util.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class IntentUtils {
    public static int getIntExtra(Fragment fragment, String key) {
        return getIntent(fragment).getIntExtra(key, Integer.MIN_VALUE);
    }

    public static String getStringExtra(Fragment fragment, String key) {
        return getIntent(fragment).getStringExtra(key);
    }

    public static <T extends Parcelable> T getParcelableExtra(Fragment fragment, String key) {
        return getIntent(fragment).getParcelableExtra(key);
    }

    public static <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(Fragment fragment, String key) {
        return getIntent(fragment).getParcelableArrayListExtra(key);
    }

    private static Intent getIntent(Fragment fragment) {
        return fragment.requireActivity().getIntent();
    }
}