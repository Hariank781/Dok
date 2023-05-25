package com.example.doktor;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    private boolean isFullScreen = false;
    ImageButton mFullScreenButton;
    private Context mContext;
    private PlayerView mExoPlayerView;
    private TextView mTitleTextView;
    private SimpleExoPlayer mExoPlayer;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mExoPlayerView = itemView.findViewById(R.id.exoplayer_view);
        mTitleTextView = itemView.findViewById(R.id.videotitle);
        mFullScreenButton = itemView.findViewById(R.id.fullscreen_button);
    }

    public void setVideo(VideoItem videoItem) {
        mTitleTextView.setText(videoItem.getTitle());

        if (mExoPlayer == null) {
            // Create a new ExoPlayer instance
            TrackSelector trackSelector = new DefaultTrackSelector(mContext);
            mExoPlayer = new SimpleExoPlayer.Builder(mContext).setTrackSelector(trackSelector).build();
            mExoPlayerView.setPlayer(mExoPlayer);
        } else {
            mExoPlayer.stop();
        }


        // Create a media source for the video URL
        Uri videoUri = Uri.parse(videoItem.getUrl());
        MediaSource mediaSource = buildMediaSource(videoUri);

        // Prepare the player with the media source
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false); // Auto-play is set to false

        // Set a click listener on the player view to start playback when clicked
        mExoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExoPlayer.getPlayWhenReady()) {
                    mExoPlayer.setPlayWhenReady(false);
                } else {
                    mExoPlayer.setPlayWhenReady(true);
                }
            }
        });
    }

    public void toggleFullScreen() {
        isFullScreen = !isFullScreen;
        if (isFullScreen) {
            // Enter full-screen mode
            mFullScreenButton.setImageResource(R.drawable.youtobe);
            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        } else {
            // Exit full-screen mode
            mFullScreenButton.setImageResource(R.drawable.youtobe);
            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
    }


    private MediaSource buildMediaSource(Uri uri) {
        // Create a ProgressiveMediaSource pointing to the video URL
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, getHttpDataSourceFactory());
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
    }

    private HttpDataSource.Factory getHttpDataSourceFactory() {
        return new DefaultHttpDataSource.Factory();
    }
}