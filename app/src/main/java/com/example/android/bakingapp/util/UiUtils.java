package com.example.android.bakingapp.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Objects;

public final class UiUtils {
    public static String getFragmentArgument(Fragment fragment, String key) {
        Bundle bundle = fragment.getArguments();
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }

        return null;
    }

    public static String getActivityIntentExtra(Activity activity, String key) {
        Bundle bundle = Objects.requireNonNull(activity).getIntent().getExtras();

        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }

        return null;
    }
}