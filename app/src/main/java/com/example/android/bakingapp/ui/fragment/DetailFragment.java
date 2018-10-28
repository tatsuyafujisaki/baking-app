package com.example.android.bakingapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentDetailBinding;
import com.example.android.bakingapp.dummy.DummyContent;
import com.example.android.bakingapp.util.UiUtils;

import dagger.android.support.AndroidSupportInjection;

public class DetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private DummyContent.DummyItem mItem;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);

        String id = getResources().getBoolean(R.bool.is_tablet)
                ? UiUtils.getFragmentArgument(this, ARG_ITEM_ID)
                : UiUtils.getActivityIntentExtra(getActivity(), ARG_ITEM_ID);

        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mItem = DummyContent.ITEM_MAP.get(id);

        binding.textView.setText(mItem.details);

        return binding.getRoot();
    }

}