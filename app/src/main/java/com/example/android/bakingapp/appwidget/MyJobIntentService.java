package com.example.android.bakingapp.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.ui.IntentBuilder;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.example.android.bakingapp.ui.fragment.RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;

public class MyJobIntentService extends JobIntentService {
    private static final String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    private static AppWidgetManager appWidgetManager;
    private static int[] appWidgetIds;

    @Inject
    RecipeViewModel recipeViewModel;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    static void start(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyJobIntentService.appWidgetManager = appWidgetManager;
        MyJobIntentService.appWidgetIds = appWidgetIds;

        Intent intent = new Intent(context, MyJobIntentService.class);
        intent.setAction(APPWIDGET_UPDATE);

        int jobId = 0;
        enqueueWork(context, MyJobIntentService.class, jobId, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case APPWIDGET_UPDATE:
                ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

                if (response.isSuccessful) {
                    response.data.observeForever(recipes -> {
                        if (!Objects.requireNonNull(recipes).isEmpty()) {
                            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.app_widget);

                            // notifyAppWidgetViewDataChanged(...) should be called after remoteViews.setRemoteAdapter(...)
                            remoteViews.setRemoteAdapter(R.id.app_widget_list_view, new Intent(this, MyRemoteViewsService.class));
                            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.app_widget_list_view);

                            final int requestCode = 0;

                            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode,
                                    new IntentBuilder(this, RecipeDetailActivity.class).putParcelable(RECIPE_PARCELABLE_EXTRA_KEY, recipes.get(0)).build(), 0);

                            remoteViews.setOnClickPendingIntent(R.id.app_widget_recipe_name_text_view, pendingIntent);
                            remoteViews.setPendingIntentTemplate(R.id.app_widget_list_view, pendingIntent);

                            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                        }
                    });
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }
}