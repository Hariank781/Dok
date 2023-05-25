package com.example.doktor;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity5 extends AppCompatActivity implements AudioAdapter.OnAudioItemClickListener {
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    private List<AudioItem> audioItems;
    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        recyclerView = findViewById(R.id.audioRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioItems = new ArrayList<>();
        audioAdapter = new AudioAdapter(audioItems, this);
        recyclerView.setAdapter(audioAdapter);

        playerManager = new PlayerManager(this);
        loadAudioItems();
    }

    private void loadAudioItems() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query audioQuery = db.collection("Audios");
        audioQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            audioItems.clear(); // Clear the existing list

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String title = documentSnapshot.getString("title");
                String url = documentSnapshot.getString("url");

                AudioItem audioItem = new AudioItem(title, url);
                audioItems.add(audioItem);
            }
            audioAdapter.notifyDataSetChanged(); // Notify the adapter of data change
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load audio items.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAudioItemClick(AudioItem audioItem) {
        playerManager.playAudio(audioItem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerManager.stopAudioPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.releasePlayer();
    }
}