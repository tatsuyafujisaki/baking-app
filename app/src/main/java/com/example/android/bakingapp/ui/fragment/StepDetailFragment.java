package com.example.android.bakingapp.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.android.bakingapp.room.Step;
import com.example.android.bakingapp.room.entity.Recipe;
import com.example.android.bakingapp.util.CollectionUtils;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentUtils;
import com.example.android.bakingapp.util.ui.ViewUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class StepDetailFragment extends Fragment implements Player.EventListener {
    public static final String RECIPE_PARCELABLE_EXTRA_KEY = "RECIPE_PARCELABLE_EXTRA_KEY";
    public static final String STEP_INDEX_INT_EXTRA_KEY = "STEP_INDEX_INT_EXTRA_KEY";
    private static final String PLAY_WHEN_READY_BOOL_EXTRA_KEY = "PLAY_WHEN_READY_BOOL_EXTRA_KEY";
    private static final String CURRENT_POSITION_LONG_EXTRA_KEY = "CURRENT_POSITION_LONG_EXTRA_KEY";

    @Inject
    ExtractorMediaSource.Factory factory;

    @Inject
    boolean isTablet;

    private Context context;
    private FragmentStepDetailBinding binding;
    private Recipe recipe;
    private int stepIndex;
    private boolean playWhenReady = true;
    private long currentPosition;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStepDetailBinding.inflate(inflater, container, false);

        if (isTablet) {
            recipe = FragmentUtils.getParcelable(this, RECIPE_PARCELABLE_EXTRA_KEY);
            stepIndex = FragmentUtils.getInt(this, STEP_INDEX_INT_EXTRA_KEY);
        } else {
            recipe = IntentUtils.getParcelable(this, RECIPE_PARCELABLE_EXTRA_KEY);
            stepIndex = IntentUtils.getInt(this, STEP_INDEX_INT_EXTRA_KEY);
        }

        requireActivity().setTitle(recipe.name);

        // savedInstanceState is not null if onCreateView() is being called due to device rotation.
        if (savedInstanceState != null) {
            stepIndex = savedInstanceState.getInt(STEP_INDEX_INT_EXTRA_KEY);

            // savedInstanceState does not contain PLAY_WHEN_READY_BOOL_EXTRA_KEY if the current step of a recipe has no video.
            if (savedInstanceState.containsKey(PLAY_WHEN_READY_BOOL_EXTRA_KEY)) {
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_BOOL_EXTRA_KEY);
            }

            // savedInstanceState does not contain CURRENT_POSITION_LONG_EXTRA_KEY if the current step of a recipe has no video.
            if (savedInstanceState.containsKey(CURRENT_POSITION_LONG_EXTRA_KEY)) {
                currentPosition = savedInstanceState.getLong(CURRENT_POSITION_LONG_EXTRA_KEY);
            }
        }

        binding.stepDescriptionTextView.setText(recipe.steps.get(stepIndex).description);


        if (isTablet) {
            ViewUtils.remove(binding.bottomNavigationView);
        } else {
            initializeBottomNavigationView();

            binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.previous_step:
                        stepIndex--;
                        break;
                    case R.id.next_step:
                        stepIndex++;
                        break;
                    default:
                        throw new IllegalStateException();
                }

                initializeBottomNavigationView();
                binding.stepDescriptionTextView.setText(recipe.steps.get(stepIndex).description);

                initializePlayer();

                return true;
            });
        }

        return binding.getRoot();
    }

    private void initializeBottomNavigationView() {
        Menu menu = binding.bottomNavigationView.getMenu();
        menu.findItem(R.id.previous_step).setVisible(stepIndex > 0);
        menu.findItem(R.id.next_step).setVisible(stepIndex < recipe.steps.size() - 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer() {
        SimpleExoPlayer player = (SimpleExoPlayer) binding.playerView.getPlayer();

        // If a user navigates from an adjacent step, player is not null with the MediaSource of an adjacent step.
        if (player != null) {
            releasePlayer();
        }

        String url = getVideoUrl();

        if (TextUtils.isEmpty(url)) {
            collapseExoPlayer();
            return;
        }

        showProgressBar();

        player = ExoPlayerFactory.newSimpleInstance(context);
        player.addListener(this);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentPosition);
        player.prepare(factory.createMediaSource(Uri.parse(url)), false, true);
        binding.playerView.setPlayer(player);
    }

    private void releasePlayer() {
        Player player = binding.playerView.getPlayer();

        if (player != null) {
            player.release();
            binding.playerView.setPlayer(null);
        }

        playWhenReady = true;
        currentPosition = 0;

        collapseExoPlayer();
    }

    private String getVideoUrl() {
        Step step = recipe.steps.get(stepIndex);
        return CollectionUtils.coalesceString(step.videoURL, step.thumbnailURL);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            showExoPlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STEP_INDEX_INT_EXTRA_KEY, stepIndex);

        SimpleExoPlayer player = (SimpleExoPlayer) binding.playerView.getPlayer();

        // player does not exist if the current step of a recipe has no video.
        if (player != null) {
            outState.putBoolean(PLAY_WHEN_READY_BOOL_EXTRA_KEY, player.getPlayWhenReady());
            outState.putLong(CURRENT_POSITION_LONG_EXTRA_KEY, player.getCurrentPosition());
        }

        super.onSaveInstanceState(outState);
    }

    private void showProgressBar() {
        binding.playerView.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showExoPlayer() {
        binding.progressBar.setVisibility(View.GONE);
        binding.playerView.setVisibility(View.VISIBLE);
    }

    private void collapseExoPlayer() {
        binding.progressBar.setVisibility(View.GONE);
        binding.playerView.setVisibility(View.GONE);
    }
}