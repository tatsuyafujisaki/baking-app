package com.example.android.bakingapp.ui.adapter;

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
import com.example.android.bakingapp.ui.fragment.StepDetailFragment;
import com.example.android.bakingapp.util.ui.FragmentUtils;

import java.util.List;

public final class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<Step> steps;

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
            Fragment fragment = new StepDetailFragment();
            FragmentUtils.setArguments(fragment, steps.get(getAdapterPosition()));
            FragmentUtils.replace(fragmentManager,  R.id.step_detail_fragment_placer_holder, fragment);
        }
    }
}