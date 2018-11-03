package com.example.android.bakingapp.util.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

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

    public FragmentBuilder putParcelable(String key, Parcelable value) {
        arguments.putParcelable(key, value);
        return this;
    }

    public FragmentBuilder putParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        arguments.putParcelableArrayList(key, value);
        return this;
    }

    public Fragment build() {
        fragment.setArguments(arguments);
        return fragment;
    }
}