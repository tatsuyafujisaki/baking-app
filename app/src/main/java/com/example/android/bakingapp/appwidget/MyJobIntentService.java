package com.example.android.bakingapp.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseIntArray;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.room.repository.RecipeRepository;
import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.example.android.bakingapp.ui.fragment.RecipeDetailFragment;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.ui.IntentUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MyJobIntentService extends JobIntentService {
    static final String CREATE_WIDGETS_ACTION = "CREATE_WIDGETS_ACTION";
    static final String RESIZE_WIDGET_ACTION = "RESIZE_WIDGET_ACTION";
    static final String SHOW_NEXT_RECIPE_WIDGET_ACTION = "SHOW_NEXT_RECIPE_WIDGET_ACTION";
    private static final String APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY = "APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY";
    private static final SparseIntArray appWidgetIdRecipeIndexMap = new SparseIntArray();

    @Inject
    RecipeRepository recipeRepository;

    static void enqueueWork(Context context, String action, int[] appWidgetIds) {
        enqueueWork(context, MyJobIntentService.class, 0 /* jobId */,
                new Intent(context, MyJobIntentService.class)
                        .setAction(action)
                        .putExtra(APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY, appWidgetIds));
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String action = IntentUtils.requireAction(intent);
        int[] appWidgetIds = IntentUtils.getIntArray(intent, APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY);

        ApiResponse<LiveData<List<Recipe>>> response = recipeRepository.getRecipes();

        if (!response.isSuccessful) {
            return;
        }

        response.data.observeForever(recipes -> {
            if (recipes == null || recipes.isEmpty()) {
                return;
            }

            int recipeCount = recipes.size();

            for (int appWidgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.app_widget);

                int recipeIndex;

                switch (action) {
                    case CREATE_WIDGETS_ACTION:
                        recipeIndex = 0;
                        appWidgetIdRecipeIndexMap.put(appWidgetId, recipeIndex);
                        break;
                    case RESIZE_WIDGET_ACTION:
                        recipeIndex = appWidgetIdRecipeIndexMap.get(appWidgetId);
                        break;
                    case SHOW_NEXT_RECIPE_WIDGET_ACTION:
                        recipeIndex = (appWidgetIdRecipeIndexMap.get(appWidgetId) + 1) % recipeCount;
                        appWidgetIdRecipeIndexMap.put(appWidgetId, recipeIndex);
                        break;
                    default:
                        throw new IllegalStateException();
                }

                Recipe recipe = recipes.get(recipeIndex);

                remoteViews.setTextViewText(R.id.recipe_name_text_view, recipe.name);
                remoteViews.setRemoteAdapter(R.id.ingredients_list_view, new Intent(this, MyRemoteViewsService.class));

                setPendingIntentForLaunch(appWidgetId, remoteViews, recipe);
                setPendingIntentForNextRecipe(appWidgetId, remoteViews);

                sendBroadcastForUpdateListView(appWidgetId, recipe);

                AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, remoteViews);
            }
        });
    }

    private void setPendingIntentForLaunch(int appWidgetId, RemoteViews remoteViews, Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class)
                .putExtra(RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY, recipe);

        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(appWidgetId, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.recipe_name_text_view, pendingIntent);
        remoteViews.setPendingIntentTemplate(R.id.ingredients_list_view, pendingIntent);
    }

    private void setPendingIntentForNextRecipe(int appWidgetId, RemoteViews remoteViews) {
        Intent intent = new Intent(this, MyAppWidgetProvider.class)
                .setAction(MyAppWidgetProvider.SHOW_NEXT_RECIPE_ACTION)
                .putExtra(MyAppWidgetProvider.APP_WIDGET_ID_INT_EXTRA_KEY, appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_recipe_image_view, pendingIntent);
    }

    private void sendBroadcastForUpdateListView(int appWidgetId, Recipe recipe) {
        Intent intent = new Intent(MyRemoteViewsFactory.SEND_RECIPE_ID_ACTION)
                .putExtra(MyRemoteViewsFactory.APP_WIDGET_ID_INT_EXTRA_KEY, appWidgetId)
                .putExtra(MyRemoteViewsFactory.RECIPE_PARCELABLE_EXTRA_KEY, recipe);

        sendBroadcast(intent);
    }
}