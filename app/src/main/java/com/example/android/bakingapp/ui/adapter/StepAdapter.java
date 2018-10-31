package com.example.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.StepViewHolderBinding;
import com.example.android.bakingapp.room.Step;

import java.util.List;

public final class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private final List<Step> steps;

    public StepAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(StepViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.binding.nameTextView.setText(step.description);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final StepViewHolderBinding binding;

        ViewHolder(StepViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}