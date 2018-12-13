package com.example.android.bakingapp.ui.activity;

import android.os.Bundle;

import com.example.android.bakingapp.R;

import androidx.appcompat.app.AppCompatActivity;
import dagger.android.AndroidInjection;

public class StepDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
    }
}