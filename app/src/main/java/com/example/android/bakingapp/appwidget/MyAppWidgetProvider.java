package com.example.android.bakingapp.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingapp.util.ui.IntentUtils;

import static com.example.android.bakingapp.appwidget.MyJobIntentService.CREATE_WIDGETS_ACTION;
import static com.example.android.bakingapp.appwidget.MyJobIntentService.RESIZE_WIDGET_ACTION;
import static com.example.android.bakingapp.appwidget.MyJobIntentService.SHOW_NEXT_RECIPE_WIDGET_ACTION;

public class MyAppWidgetProvider extends AppWidgetProvider {
    static final String SHOW_NEXT_RECIPE_ACTION = "UPDATE_APP_WIDGET_WITH_NEXT_RECIPE_ACTION";
    private static final String APP_WIDGET_ID_INT_EXTRA_KEY = "APP_WIDGET_ID_INT_EXTRA_KEY";

    // Called after an app widget is created or android:updatePeriodMillis is reached
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyJobIntentService.enqueueWork(context, CREATE_WIDGETS_ACTION, appWidgetIds);
    }

    // Called after an app widget is resized
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        MyJobIntentService.enqueueWork(context, RESIZE_WIDGET_ACTION, new int[]{appWidgetId});
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        switch (IntentUtils.requireAction(intent)) {
            case SHOW_NEXT_RECIPE_ACTION:
                int appWidgetId = IntentUtils.getInt(intent, APP_WIDGET_ID_INT_EXTRA_KEY);
                MyJobIntentService.enqueueWork(context, SHOW_NEXT_RECIPE_WIDGET_ACTION, new int[]{appWidgetId});
                break;
        }
    }
}