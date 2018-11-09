package com.example.android.bakingapp.appwidget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.Ingredient;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.util.ApiResponse;
import com.example.android.bakingapp.util.ui.IntentUtils;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;
import java.util.Objects;

public class MyRemoteViewsFactory extends BroadcastReceiver implements RemoteViewsService.RemoteViewsFactory {
    static final String SEND_RECIPE_ID_ACTION = "com.example.android.bakingapp.appwidget.action.SEND_RECIPE_ID_ACTION";
    static final String APP_WIDGET_ID_INT_EXTRA_KEY = "APP_WIDGET_ID_INT_EXTRA_KEY";
    static final String RECIPE_INDEX_INT_EXTRA_KEY = "RECIPE_INDEX_INT_EXTRA_KEY";
    private Context context;
    private RecipeViewModel recipeViewModel;
    private Recipe recipe;
    private int recipeIndex;

    public MyRemoteViewsFactory() {
    }

    MyRemoteViewsFactory(Context context, RecipeViewModel recipeViewModel) {
        this.context = context;
        this.recipeViewModel = recipeViewModel;
    }

    // Called after AppWidgetManager.updateAppWidgets(...)
    @Override
    public void onCreate() {
        context.registerReceiver(this, new IntentFilter(SEND_RECIPE_ID_ACTION));
    }

    // Called after onCreate() as well as after AppWidgetManager.notifyAppWidgetViewDataChanged(...)
    @Override
    public void onDataSetChanged() {
        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if (response.isSuccessful) {
            response.data.observeForever(recipes -> {
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    recipe = recipes.get(recipeIndex);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        context.unregisterReceiver(this);
    }

    @Override
    public int getCount() {
        return recipe != null ? recipe.ingredients.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = recipe.ingredients.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredient_view_holder);
        remoteViews.setTextViewText(R.id.ingredient_text_view, ingredient.ingredient);
        remoteViews.setTextViewText(R.id.quantity_text_view, String.valueOf(ingredient.quantity));
        remoteViews.setTextViewText(R.id.measure_text_view, ingredient.measure);

        // Setting an empty Intent to setOnClickFillInIntent(...) seems unnecessary but required to fire a PendingIntent specified in remoteViews.setPendingIntentTemplate(...)
        Intent intent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.ingredient_text_view, intent);
        remoteViews.setOnClickFillInIntent(R.id.quantity_text_view, intent);
        remoteViews.setOnClickFillInIntent(R.id.measure_text_view, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (IntentUtils.requireAction(intent)) {
            case SEND_RECIPE_ID_ACTION:
                recipeIndex = IntentUtils.getInt(intent, RECIPE_INDEX_INT_EXTRA_KEY);
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                        IntentUtils.getInt(intent, APP_WIDGET_ID_INT_EXTRA_KEY),
                        R.id.ingredients_list_view);
                break;
            default:
                throw new IllegalStateException();
        }
    }
}