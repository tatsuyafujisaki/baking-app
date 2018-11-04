package com.example.android.bakingapp.util.ui;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {
    public static void remove(View view) {
        ((ViewGroup) view.getParent()).removeView(view);
    }
}