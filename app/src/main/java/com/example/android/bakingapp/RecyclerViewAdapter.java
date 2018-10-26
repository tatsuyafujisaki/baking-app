package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.databinding.RecyclerviewItemBinding;
import com.example.android.bakingapp.dummy.DummyContent;

import java.util.List;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<DummyContent.DummyItem> items;
    private final boolean isTwoPane;

    RecyclerViewAdapter(FragmentManager fragmentManager, List<DummyContent.DummyItem> items, boolean isTwoPane) {
        this.items = items;
        this.fragmentManager = fragmentManager;
        this.isTwoPane = isTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.idTextView.setText(items.get(position).id);
        holder.binding.contentTextView.setText(items.get(position).content);
        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final RecyclerviewItemBinding binding;

        ViewHolder(RecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) v.getTag();

            if (isTwoPane) {
                Bundle bundle = new Bundle();
                bundle.putString(DetailFragment.ARG_ITEM_ID, item.id);

                Fragment fragment = new DetailFragment();
                fragment.setArguments(bundle);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                // TODO: Check why DetailFragment in tablet is not redrawn.
                fragmentManager
                        .beginTransaction()
                        .detach(fragmentManager.findFragmentById(R.id.detail_fragment))
                        .attach(detailFragment)
                        .commit();
            } else {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailFragment.ARG_ITEM_ID, item.id);
                context.startActivity(intent);
            }
        }
    }
}