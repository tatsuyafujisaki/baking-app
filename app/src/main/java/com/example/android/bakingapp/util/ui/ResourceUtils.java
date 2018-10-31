package com.example.android.bakingapp.util.ui;

import android.content.res.Resources;

import com.example.android.bakingapp.R;

public class ResourceUtils {
    public static int getGridColumnSpan(Resources resources, int gridColumnWidthResId) {
        return Math.max(1, resources.getDisplayMetrics().widthPixels / resources.getInteger(gridColumnWidthResId));
    }

    public static boolean isTablet(Resources resources) {
        return resources.getBoolean(R.bool.is_tablet);
    }
}