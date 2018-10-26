package com.example.android.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.databinding.ActivityMainBinding;
import com.example.android.bakingapp.dummy.DummyContent;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());
        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        boolean isTwoPane = getResources().getBoolean(R.bool.is_tablet);

        binding.include.stepRecyclerView.setAdapter(new RecyclerViewAdapter(getSupportFragmentManager(), DummyContent.ITEMS, isTwoPane));
    }
}