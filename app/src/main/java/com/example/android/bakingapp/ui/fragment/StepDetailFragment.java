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
import com.example.android.bakingapp.util.ListUtils;
import com.example.android.bakingapp.util.ui.FragmentUtils;
import com.example.android.bakingapp.util.ui.IntentUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class StepDetailFragment extends Fragment implements Player.EventListener {
    public static final String STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY = "STEPS";
    public static final String STEP_INDEX_INT_EXTRA_KEY = "STEP_INDEX";
    private static final String PLAY_WHEN_READY_BOOL_EXTRA_KEY = "PLAY_WHEN_READY";
    private static final String CURRENT_POSITION_LONG_EXTRA_KEY = "CURRENT_POSITION";

    @Inject
    ExtractorMediaSource.Factory factory;
    private FragmentStepDetailBinding binding;
    private List<Step> steps;
    private int stepIndex;
    private boolean playWhenReady = true;
    private long currentPosition;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStepDetailBinding.inflate(inflater, container, false);

        if (FragmentUtils.hasArguments(this)) {
            steps = FragmentUtils.getParcelableArrayList(this, STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY);

            if (savedInstanceState == null) {
                stepIndex = FragmentUtils.getInt(this, STEP_INDEX_INT_EXTRA_KEY);
            }
        } else {
            steps = IntentUtils.getParcelableArrayListExtra(this, STEPS_PARCELABLE_ARRAY_LIST_EXTRA_KEY);

            if (savedInstanceState == null) {
                stepIndex = IntentUtils.getIntExtra(this, STEP_INDEX_INT_EXTRA_KEY);
            }
        }

        if (savedInstanceState != null) {
            stepIndex = savedInstanceState.getInt(STEP_INDEX_INT_EXTRA_KEY);

            if (savedInstanceState.containsKey(PLAY_WHEN_READY_BOOL_EXTRA_KEY)) {
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_BOOL_EXTRA_KEY);
            }

            if (savedInstanceState.containsKey(CURRENT_POSITION_LONG_EXTRA_KEY)) {
                currentPosition = savedInstanceState.getLong(CURRENT_POSITION_LONG_EXTRA_KEY);
            }
        }

        setBottomNavigationView();
        binding.stepDescriptionTextView.setText(steps.get(stepIndex).description);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.previous_step:
                    stepIndex--;
                    break;
                case R.id.next_step:
                    stepIndex++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            setBottomNavigationView();
            binding.stepDescriptionTextView.setText(steps.get(stepIndex).description);

            playWhenReady = true;
            currentPosition = 0;

            initializePlayer();

            return true;
        });

        return binding.getRoot();
    }

    private void setBottomNavigationView() {
        Menu menu = binding.bottomNavigationView.getMenu();
        menu.findItem(R.id.previous_step).setVisible(stepIndex > 0);
        menu.findItem(R.id.next_step).setVisible(stepIndex < steps.size() - 1);
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
        String url = getVideoUrl();

        if (TextUtils.isEmpty(url)) {
            releasePlayer();
            return;
        }

        SimpleExoPlayer player = (SimpleExoPlayer) binding.playerView.getPlayer();

        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext());
            player.addListener(this);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentPosition);
            binding.playerView.setVisibility(View.INVISIBLE);
            binding.playerView.setPlayer(player);
            player.prepare(factory.createMediaSource(Uri.parse(url)), false, true);
            return;
        }

        if (currentPosition > 0) {
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentPosition);
        }
    }

    private void releasePlayer() {
        Player player = binding.playerView.getPlayer();

        if (player != null) {
            player.release();
            binding.playerView.setPlayer(null);
        }

        binding.playerView.setVisibility(View.GONE);
    }

    private String getVideoUrl() {
        Step step = steps.get(stepIndex);
        return ListUtils.coalesceString(step.videoURL, step.thumbnailURL);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            binding.playerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STEP_INDEX_INT_EXTRA_KEY, stepIndex);

        SimpleExoPlayer player = (SimpleExoPlayer) binding.playerView.getPlayer();

        if (player != null) {
            outState.putBoolean(PLAY_WHEN_READY_BOOL_EXTRA_KEY, player.getPlayWhenReady());
            outState.putLong(CURRENT_POSITION_LONG_EXTRA_KEY, player.getCurrentPosition());
        }

        super.onSaveInstanceState(outState);
    }
}