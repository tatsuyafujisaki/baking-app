package com.example.android.bakingapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityDetailBinding;
import com.example.android.bakingapp.dummy.DummyContent;
import com.example.android.bakingapp.util.UiUtils;

import java.util.Objects;

import dagger.android.AndroidInjection;

import static com.example.android.bakingapp.ui.fragment.DetailFragment.ARG_ITEM_ID;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // Set toolbar
        setSupportActionBar(binding.toolbar);

        binding.collapsingToolbarLayout.setTitle(DummyContent.ITEM_MAP.get(UiUtils.getActivityIntentExtra(this, ARG_ITEM_ID)).content);

        // Show Home (a.k.a. Up) button on toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}