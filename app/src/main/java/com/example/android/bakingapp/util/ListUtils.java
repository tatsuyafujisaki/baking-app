package com.example.android.bakingapp.util;

import android.text.TextUtils;

public final class ListUtils {
    public static String coalesce(String... ss) {
        for (String s : ss) {
            if (!TextUtils.isEmpty(s)) {
                return s;
            }
        }

        return null;
    }
}