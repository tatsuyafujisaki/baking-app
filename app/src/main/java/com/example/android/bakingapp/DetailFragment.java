package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.FragmentDetailBinding;
import com.example.android.bakingapp.dummy.DummyContent;

public class DetailFragment extends Fragment {
    static final String ARG_ITEM_ID = "item_id";

    private DummyContent.DummyItem mItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);

        String id = null;

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ARG_ITEM_ID)) {
            id = bundle.getString(ARG_ITEM_ID);
        } else {
            bundle = getActivity().getIntent().getExtras();

            if (bundle != null && bundle.containsKey(ARG_ITEM_ID)) {
                id = bundle.getString(ARG_ITEM_ID);
            }
        }

        if (id != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(id);

            CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }

            binding.textView.setText(mItem.details);
        }

        return binding.getRoot();
    }
}