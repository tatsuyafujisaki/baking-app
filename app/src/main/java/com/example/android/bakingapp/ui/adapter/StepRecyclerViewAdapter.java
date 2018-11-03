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
import com.example.android.bakingapp.databinding.StepRecyclerViewItemBinding;
import com.example.android.bakingapp.room.Step;
import com.example.android.bakingapp.ui.activity.StepDetailActivity;
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;
import com.example.android.bakingapp.util.converter.Converter;
import com.example.android.bakingapp.util.ui.FragmentBuilder;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentBuilder;
import com.example.android.bakingapp.util.ui.ResourceUtils;

import java.util.List;

public final class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {
    private final List<Step> steps;
    private final FragmentManager fragmentManager;

    public StepRecyclerViewAdapter(FragmentManager fragmentManager, List<Step> steps) {
        this.fragmentManager = fragmentManager;
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StepRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.binding.nameTextView.setText(step.shortDescription);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final StepRecyclerViewItemBinding binding;

        ViewHolder(StepRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (ResourceUtils.isTablet(v.getResources())) {
                Fragment fragment  = new FragmentBuilder(new StepDetailFragment())
                        .putParcelableArrayList(StepDetailFragment.STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY, Converter.toArrayList(steps))
                        .putInt(StepDetailFragment.STEP_INTDEX_INT_EXTRA_KEY, getAdapterPosition())
                        .build();

                FragmentUtils.replace(fragmentManager, R.id.step_detail_fragment_container, fragment);
            } else {
                Context context = v.getContext();

                Intent intent = new IntentBuilder(context, StepDetailActivity.class)
                    .putParcelableArrayListExtra(StepDetailFragment.STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY, steps)
                    .putExtra(StepDetailFragment.STEP_INTDEX_INT_EXTRA_KEY, getAdapterPosition())
                    .build();

                context.startActivity(intent);
            }
        }
    }
}