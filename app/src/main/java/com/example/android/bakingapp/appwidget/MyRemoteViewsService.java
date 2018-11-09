package com.example.android.bakingapp.appwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MyRemoteViewsService extends RemoteViewsService {
    @Inject
    Context context;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactory(context);
    }
}