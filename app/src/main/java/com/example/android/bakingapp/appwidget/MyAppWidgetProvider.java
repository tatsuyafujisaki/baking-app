package com.example.android.bakingapp.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;

public class MyAppWidgetProvider extends AppWidgetProvider {
    // Called after an app widget is created or android:updatePeriodMillis is reached
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyJobIntentService.start(context, appWidgetManager, appWidgetIds);
    }

    // Called after an app widget is resized
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        MyJobIntentService.start(context, appWidgetManager, new int[]{appWidgetId});
    }
}
