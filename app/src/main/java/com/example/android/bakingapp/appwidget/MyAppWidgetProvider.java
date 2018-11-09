package com.example.android.bakingapp.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingapp.util.ui.IntentUtils;

import java.util.Objects;

public class MyAppWidgetProvider extends AppWidgetProvider {
    static final String NAVIGATE_TO_NEXT_RECIPE = "NAVIGATE_TO_NEXT_RECIPE";
    static final String RECIPE_ID_INT_EXTRA_KEY = "RECIPE_ID_INT_EXTRA_KEY";

    // Called after an app widget is created or android:updatePeriodMillis is reached
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyJobIntentService.start(context, appWidgetIds);
    }

    // Called after an app widget is resized
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        MyJobIntentService.start(context, new int[]{appWidgetId});
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(Objects.requireNonNull(intent.getAction()).equals(NAVIGATE_TO_NEXT_RECIPE)) {
            MyJobIntentService.start(context, IntentUtils.getInt(intent, RECIPE_ID_INT_EXTRA_KEY));
        }
    }
}