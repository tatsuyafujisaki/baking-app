package com.example.android.bakingapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.StepViewHolderBinding;
import com.example.android.bakingapp.room.Step;
import com.example.android.bakingapp.ui.activity.StepDetailActivity;
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;
import com.example.android.bakingapp.util.converter.Converter;
import com.example.android.bakingapp.util.ui.FragmentBuilder;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentBuilder;

import java.util.List;

public class StepViewHolderAdapter extends RecyclerView.Adapter<StepViewHolderAdapter.ViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<Step> steps;
    private final boolean isTablet;

    public StepViewHolderAdapter(FragmentManager fragmentManager, List<Step> steps, boolean isTablet) {
        this.fragmentManager = fragmentManager;
        this.steps = steps;
        this.isTablet = isTablet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StepViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.binding.nameTextView.setText(step.shortDescription);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final StepViewHolderBinding binding;

        ViewHolder(StepViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isTablet) {
                Fragment fragment = new FragmentBuilder(new StepDetailFragment())
                        .putParcelableArrayList(StepDetailFragment.STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY, Converter.toArrayList(steps))
                        .putInt(StepDetailFragment.STEP_INDEX_INT_EXTRA_KEY, getAdapterPosition())
                        .build();

                FragmentUtils.replace(fragmentManager, R.id.step_detail_fragment_container, fragment);
            } else {
                Context context = v.getContext();

                Intent intent = new IntentBuilder(context, StepDetailActivity.class)
                        .putParcelableArrayListExtra(StepDetailFragment.STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY, steps)
                        .putExtra(StepDetailFragment.STEP_INDEX_INT_EXTRA_KEY, getAdapterPosition())
                        .build();

                context.startActivity(intent);
            }
        }
    }
}