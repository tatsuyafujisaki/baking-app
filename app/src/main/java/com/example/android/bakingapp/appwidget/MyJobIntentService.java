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
import com.example.android.bakingapp.util.ui.IntentUtils;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.example.android.bakingapp.ui.fragment.RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;

public class MyJobIntentService extends JobIntentService {
    private static final String UPDATE_APP_WIDGET = "UPDATE_APP_WIDGET";
    private static final String NAVIGATE_TO_NEXT_RECIPE = "NAVIGATE_TO_NEXT_RECIPE";
    private static final String RECIPE_ID_INT_EXTRA_KEY = "RECIPE_ID_INT_EXTRA_KEY";

    private static AppWidgetManager appWidgetManager;
    private static int[] appWidgetIds;

    @Inject
    RecipeViewModel recipeViewModel;

    static void start(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyJobIntentService.appWidgetManager = appWidgetManager;
        MyJobIntentService.appWidgetIds = appWidgetIds;

        enqueueWork(context, MyJobIntentService.class, 0 /* jobId */,
                new IntentBuilder(context, MyJobIntentService.class).setAction(UPDATE_APP_WIDGET).build());
    }

    static void start(Context context, int recipeId) {
        enqueueWork(context, MyJobIntentService.class, 0 /* jobId */,
                new IntentBuilder(context, MyJobIntentService.class)
                        .setAction(NAVIGATE_TO_NEXT_RECIPE)
                        .putInt(RECIPE_ID_INT_EXTRA_KEY, recipeId)
                        .build());
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int recipeId;

        switch (Objects.requireNonNull(intent.getAction())) {
            case UPDATE_APP_WIDGET:
                recipeId = 0;
                break;
            case NAVIGATE_TO_NEXT_RECIPE:
                recipeId = IntentUtils.getIntExtra(intent, RECIPE_ID_INT_EXTRA_KEY);
                break;
            default:
                throw new IllegalStateException();
        }

        updateAppWidget(recipeId);
    }

    private void updateAppWidget(int recipeId) {
        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if (response.isSuccessful) {
            response.data.observeForever(recipes -> {
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.app_widget);

                    Recipe recipe = recipes.get(recipeId);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */,
                            new IntentBuilder(this, RecipeDetailActivity.class).putParcelable(RECIPE_PARCELABLE_EXTRA_KEY, recipe).build(),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    remoteViews.setTextViewText(R.id.recipe_name_text_view, recipe.name);
                    remoteViews.setOnClickPendingIntent(R.id.recipe_name_text_view, pendingIntent);
                    remoteViews.setPendingIntentTemplate(R.id.ingredients_list_view, pendingIntent);

                    remoteViews.setOnClickPendingIntent(R.id.next_recipe_image_view,
                            PendingIntent.getBroadcast(this, 0 /* requestCode */,
                                    new IntentBuilder(this, MyAppWidgetProvider.class).setAction(MyAppWidgetProvider.NAVIGATE_TO_NEXT_RECIPE).putInt(MyAppWidgetProvider.RECIPE_ID_INT_EXTRA_KEY, (recipeId + 1) % recipes.size()).build(),
                                    PendingIntent.FLAG_UPDATE_CURRENT));

                    remoteViews.setRemoteAdapter(R.id.ingredients_list_view, new Intent(this, MyRemoteViewsService.class));

                    // sendBroadcast(...) should be called after remoteViews.setRemoteAdapter(...) for MyRemoteViewsService to receive the broadcast
                    // sendBroadcast(...) should be called before appWidgetManager.notifyAppWidgetViewDataChanged because it uses recipeId embedded in the broadcast
                    sendBroadcast(new IntentBuilder(MyRemoteViewsFactory.SEND_RECIPE_ID)
                            .putInt(MyRemoteViewsFactory.RECIPE_ID_INT_EXTRA_KEY, recipeId)
                            .putIntArray(MyRemoteViewsFactory.APP_WIDGET_ID_INT_ARRAY_EXTRA_KEY, appWidgetIds)
                            .build());

                    // notifyAppWidgetViewDataChanged(...) should be called after remoteViews.setRemoteAdapter(...)
                    // appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_list_view);

                    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                }
            });
        }
    }
}