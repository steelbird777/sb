package com.example.sb;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private TextView video_name;
    private TextView video_content;
    private String video;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        video_name = findViewById(R.id.video_name);
        video_content = findViewById(R.id.video_extended_content);
        playerView = findViewById(R.id.video_player);

        fetchVideoPlayer();
    }

    public void fetchVideoPlayer() {
        db.collection("video")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Video> videos = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Video video = document.toObject(Video.class);
                            if (video != null) {
                                videos.add(video);
                            }
                        }

                        // Assuming you want to play the first video in the list
                        if (!videos.isEmpty()) {
                            Video firstVideo = videos.get(0);
                            video_name.setText(firstVideo.getVideo_name());
                            video_content.setText(firstVideo.getVideo_content());
                            String videoUrl = firstVideo.getVideo();
                            initializePlayer(videoUrl);
                        }else {
                            // Handle the case where no videos are fetched
                            Log.e(TAG, "No videos fetched");
                            Toast.makeText(VideoPlayerActivity.this, "No videos available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Error fetching video details: ", task.getException());
                        Toast.makeText(VideoPlayerActivity.this, "Error fetching videos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializePlayer(String videoUrl) {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true); // Auto-start playback
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
