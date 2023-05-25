package com.example.doktor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private List<AudioItem> audioItems;
    private OnAudioItemClickListener listener;

    public AudioAdapter(List<AudioItem> audioItems, OnAudioItemClickListener listener) {
        this.audioItems = audioItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioItem audioItem = audioItems.get(position);
        holder.bind(audioItem, listener);
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.audioTitle);
        }

        public void bind(AudioItem audioItem, OnAudioItemClickListener listener) {
            titleTextView.setText(audioItem.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAudioItemClick(audioItem);
                }
            });
        }
    }

    interface OnAudioItemClickListener {
        void onAudioItemClick(AudioItem audioItem);
    }
}