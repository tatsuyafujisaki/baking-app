package com.example.android.bakingapp.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.SparseIntArray;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.ui.IntentUtils;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.example.android.bakingapp.appwidget.MyRemoteViewsFactory.APP_WIDGET_ID_INT_EXTRA_KEY;
import static com.example.android.bakingapp.appwidget.MyRemoteViewsFactory.RECIPE_INDEX_INT_EXTRA_KEY;
import static com.example.android.bakingapp.ui.fragment.RecipeDetailFragment.RECIPE_PARCELABLE_EXTRA_KEY;

public class MyJobIntentService extends JobIntentService {
    static final String CREATE_WIDGETS_ACTION = "CREATE_WIDGETS_ACTION";
    static final String RESIZE_WIDGET_ACTION = "RESIZE_WIDGET_ACTION";
    static final String SHOW_NEXT_RECIPE_WIDGET_ACTION = "SHOW_NEXT_RECIPE_WIDGET_ACTION";
    private static final String APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY = "APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY";
    private static final SparseIntArray appWidgetIdRecipeIndexMap = new SparseIntArray();

    @Inject
    RecipeViewModel recipeViewModel;

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

        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if (!response.isSuccessful) {
            return;
        }

        response.data.observeForever(recipes -> {
            if (!Objects.requireNonNull(recipes).isEmpty()) {

                int recipeCount = recipes.size();

                for (int appWidgetId : IntentUtils.getIntArray(intent, APP_WIDGET_IDS_INT_ARRAY_EXTRA_KEY)) {
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

                    setPendingIntentForLaunch(remoteViews, recipe);
                    setPendingIntentForNextRecipe(remoteViews, appWidgetId);

                    sendBroadcastForUpdateListView(appWidgetId, recipeIndex);

                    AppWidgetManager.getInstance(this).updateAppWidget(appWidgetId, remoteViews);
                }
            }
        });
    }

    private void setPendingIntentForLaunch(RemoteViews remoteViews, Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class).putExtra(RECIPE_PARCELABLE_EXTRA_KEY, recipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.recipe_name_text_view, pendingIntent);
        remoteViews.setPendingIntentTemplate(R.id.ingredients_list_view, pendingIntent);
    }

    private void setPendingIntentForNextRecipe(RemoteViews remoteViews, int appWidgetId) {
        Intent intent = new Intent(this, MyAppWidgetProvider.class)
                .setAction(MyAppWidgetProvider.SHOW_NEXT_RECIPE_ACTION)
                .putExtra(APP_WIDGET_ID_INT_EXTRA_KEY, appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 /* requestCode */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_recipe_image_view, pendingIntent);
    }

    private void sendBroadcastForUpdateListView(int appWidgetId, int recipeIndex) {
        Intent intent = new Intent(MyRemoteViewsFactory.SEND_RECIPE_ID_ACTION)
                .putExtra(APP_WIDGET_ID_INT_EXTRA_KEY, appWidgetId)
                .putExtra(RECIPE_INDEX_INT_EXTRA_KEY, recipeIndex);

        sendBroadcast(intent);
    }
}