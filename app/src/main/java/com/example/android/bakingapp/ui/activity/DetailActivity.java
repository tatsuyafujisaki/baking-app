package com.example.android.bakingapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.ActivityDetailBinding;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // binding.collapsingToolbarLayout.setTitle(DummyContent.ITEM_MAP.get(UiUtils.getActivityIntentExtra(this, ARG_ITEM_ID)).content);
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