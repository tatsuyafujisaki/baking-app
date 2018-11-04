package com.example.android.bakingapp.dagger.module;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebSettings;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.room.RecipeDatabase;
import com.example.android.bakingapp.room.dao.RecipeDao;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.room.repository.RecipeRepository;
import com.example.android.bakingapp.util.RecipeService;
import com.example.android.bakingapp.viewmodel.RecipeViewModel;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    @Singleton
    @Provides
    static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Singleton
    @Provides
    static Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    static Resources provideResources(Context context) {
        return context.getResources();
    }

    @Singleton
    @Provides
    static RecipeDatabase provideRecipeDatabase(Context context) {
        return RecipeDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    static RecipeDao provideRecipeDao(RecipeDatabase recipeDatabase) {
        return recipeDatabase.recipeDao();
    }

    @Singleton
    @Provides
    static ViewModel provideViewModel(FragmentActivity activity, Class<RecipeViewModel> modelClass, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(activity, factory).get(modelClass);
    }

    @Singleton
    @Provides
    static String provideUserAgent(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }

    @Singleton
    @Provides
    static Gson provideGson() {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Recipe>>() {
        }.getType(), (JsonDeserializer<List<Recipe>>) (json, type, context1)
                -> new Gson().fromJson(json, type)).create();
    }

    @Singleton
    @Provides
    static RecipeService provideRecipeService(Context context, Gson gson) {

        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.recipes_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RecipeService.class);
    }

    @Singleton
    @Provides
    static RecipeRepository provideRecipeRepository(RecipeService recipeService, RecipeDao recipeDao, Executor executor) {
        return new RecipeRepository(recipeService, recipeDao, executor);
    }

    @Singleton
    @Provides
    static DefaultHttpDataSourceFactory provideDefaultHttpDataSourceFactory(String userAgent) {
        return new DefaultHttpDataSourceFactory(userAgent);
    }

    @Singleton
    @Provides
    static Factory provideFactory(DefaultHttpDataSourceFactory defaultHttpDataSourceFactory) {
        return new ExtractorMediaSource.Factory(defaultHttpDataSourceFactory);
    }

    @Singleton
    @Provides
    static boolean provideIsTablet(Resources resources) {
        return resources.getBoolean(R.bool.is_tablet);
    }

    @Singleton
    @Provides
    static int provideGridColumnSpan(Resources resources) {
        return Math.max(1, resources.getDisplayMetrics().widthPixels / resources.getInteger(R.integer.grid_column_span));
    }
}