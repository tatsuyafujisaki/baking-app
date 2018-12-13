package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.android.bakingapp.util.converter.Converter;

import java.util.List;

import androidx.fragment.app.Fragment;

public class FragmentBuilder {
    private final Fragment fragment;
    private Bundle arguments;

    public FragmentBuilder(Fragment fragment) {
        this.fragment = fragment;

        arguments = fragment.getArguments();

        if (arguments == null) {
            arguments = new Bundle();
        }
    }

    public FragmentBuilder putInt(String key, int value) {
        arguments.putInt(key, value);
        return this;
    }

    public FragmentBuilder putString(String key, String value) {
        arguments.putString(key, value);
        return this;
    }

    public FragmentBuilder putParcelable(String key, Parcelable value) {
        arguments.putParcelable(key, value);
        return this;
    }

    public FragmentBuilder putParcelableList(String key, List<? extends Parcelable> value) {
        arguments.putParcelableArrayList(key, Converter.toArrayList(value));
        return this;
    }

    public Fragment build() {
        fragment.setArguments(arguments);
        return fragment;
    }
}