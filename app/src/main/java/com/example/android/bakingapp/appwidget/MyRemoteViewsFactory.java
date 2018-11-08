package com.example.android.bakingapp.appwidget;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
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

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    static final String RECIPE_ID_INT_EXTRA_KEY = "RECIPE_ID_INT_EXTRA_KEY";

    private final Context context;
    private final RecipeViewModel recipeViewModel;
    private Recipe recipe;
    private int recipeId;

    MyRemoteViewsFactory(Context context, RecipeViewModel recipeViewModel, Intent intent) {
        this.context = context;
        this.recipeViewModel = recipeViewModel;
        recipeId = IntentUtils.getIntExtra(intent, RECIPE_ID_INT_EXTRA_KEY);
    }

    // Called after AppWidgetManager.updateAppWidget(...)
    @Override
    public void onCreate() {
    }

    // Called after onCreate() as well as after AppWidgetManager.notifyAppWidgetViewDataChanged(...)
    @Override
    public void onDataSetChanged() {
        ApiResponse<LiveData<List<Recipe>>> response = recipeViewModel.getRecipes();

        if (response.isSuccessful) {
            response.data.observeForever(recipes -> {
                if (!Objects.requireNonNull(recipes).isEmpty()) {
                    recipe = recipes.get(recipeId);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
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

        /*
         * Setting an empty Intent to setOnClickFillInIntent(...) seems unnecessary
         * but required to fire a PendingIntent specified in remoteViews.setPendingIntentTemplate(...)
         */
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
}