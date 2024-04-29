package com.example.sb;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MathsActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FrameLayout fullScreenContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maths);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize YouTubePlayerView
        youTubePlayerView = findViewById(R.id.video);
        fullScreenContainer = findViewById(R.id.full_screen_container);

        // Load video
        String videoId = "V2KCAfHjySQ"; // Extracted video ID from your YouTube URL
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            youTubePlayer.loadVideo(videoId, 0);
        });

        // Set click listener on YouTubePlayerView to toggle full-screen
        youTubePlayerView.setOnClickListener(v -> toggleFullScreen());
    }

    private void toggleFullScreen() {
        if (isInFullScreenMode()) {
            exitFullScreen();
        } else {
            enterFullScreen();
        }
    }

    private boolean isInFullScreenMode() {
        return fullScreenContainer.getVisibility() == View.VISIBLE;
    }

    private void enterFullScreen() {
        // Hide other UI elements and expand video player to full screen
        youTubePlayerView.enterFullScreen();
        fullScreenContainer.setVisibility(View.VISIBLE);
    }

    private void exitFullScreen() {
        // Show other UI elements and restore video player to normal size
        youTubePlayerView.exitFullScreen();
        fullScreenContainer.setVisibility(View.GONE);
    }
}
