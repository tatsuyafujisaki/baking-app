package com.example.android.bakingapp.util.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.example.android.bakingapp.util.converter.Converter;

import java.util.List;

public class IntentBuilder {
    private final Intent intent;

    public IntentBuilder() {
        intent = new Intent();
    }

    public IntentBuilder(Context context, Class<?> cls) {
        intent = new Intent(context, cls);
    }

    public IntentBuilder setAction(String action) {
        intent.setAction(action);
        return this;
    }

    public IntentBuilder putInt(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder putString(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder putParcelable(String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }

    public IntentBuilder putParcelableListExtra(String key, List<? extends Parcelable> value) {
        intent.putParcelableArrayListExtra(key, Converter.toArrayList(value));
        return this;
    }

    public Intent build() {
        return intent;
    }
}