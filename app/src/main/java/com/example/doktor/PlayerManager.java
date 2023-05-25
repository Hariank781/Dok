package com.example.doktor;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class PlayerManager {
    private Context context;
    private SimpleExoPlayer player;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;

    public PlayerManager(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void playAudio(AudioItem audioItem) {
        if (player == null) {
            initializePlayer();
        }

        if (requestAudioFocus()) {
            Uri audioUri = Uri.parse(audioItem.getUrl());
            MediaItem mediaItem = MediaItem.fromUri(audioUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
        } else {
            Toast.makeText(context, "Unable to gain audio focus.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(context).build();
    }

    private boolean requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int focusChange) {
                            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                                stopAudioPlayback();
                            }
                        }
                    })
                    .build();
        }

        int result = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            result = audioManager.requestAudioFocus(audioFocusRequest);
        }
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void stopAudioPlayback() {
        if (player != null) {
            player.stop();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
        }
    }
}
