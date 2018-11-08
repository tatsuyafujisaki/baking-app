package com.example.android.bakingapp.dagger.module;

import com.example.android.bakingapp.appwidget.MyJobIntentService;
import com.example.android.bakingapp.appwidget.MyRemoteViewsService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract MyJobIntentService contributeMyJobIntentService();

    @ContributesAndroidInjector
    abstract MyRemoteViewsService contributeMyRemoteViewsService();
}