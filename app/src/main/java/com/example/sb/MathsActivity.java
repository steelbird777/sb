

package com.example.sb;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.activity.EdgeToEdge;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.android.material.appbar.MaterialToolbar;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;

public class MathsActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";
    private RecyclerView videoRecyclerView;
    private FirebaseFirestore db;
    private VideoAdapter videoAdapter;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(MathsActivity.this, SubjectsActivity.class);
            startActivity(intent);
        });
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fetchVideos(userEmail);
    }

    public void fetchVideos(String userEmail) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("user_video")
                .whereEqualTo("user_mail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> video_numbers = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String video_number = document.getString("video_number");
                            video_numbers.add(video_number);
                        }
                        fetchVideoDetails(video_numbers);
                    } else {
                        Log.e(TAG, "Error fetching user videos: ", task.getException());
                        Toast.makeText(MathsActivity.this, "Error fetching videos", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    public void fetchVideoDetails(List<String> video_numbers) {
        progressBar.setVisibility(View.INVISIBLE);
        db.collection("video").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Video> videos = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Video video = document.toObject(Video.class);
                            if (video != null) {
                                videos.add(video);
                            }
                        }
                        updateRecyclerView(videos);
                    } else {
                        Log.e(TAG, "Error fetching video details: ", task.getException());
                        Toast.makeText(MathsActivity.this, "Error fetching videos", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    public void updateRecyclerView(List<Video> videos) {
        videoAdapter = new VideoAdapter(videos);
        videoRecyclerView.setAdapter(videoAdapter);
    }

    public static class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
        private List<Video> videos;

        public VideoAdapter(List<Video> videos) {
            this.videos = videos;
        }

        @NonNull
        @Override
        public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
            holder.bind(videos.get(position));
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        public static class VideoViewHolder extends RecyclerView.ViewHolder {
            private TextView video_number;
            private TextView video_name;
            private TextView video_content;
            private ImageView videoImg;

            public VideoViewHolder(@NonNull View itemView) {
                super(itemView);
                video_number = itemView.findViewById(R.id.video_number);
                video_name = itemView.findViewById(R.id.video_name);
                video_content = itemView.findViewById(R.id.video_content);
                videoImg = itemView.findViewById(R.id.videoImg);
            }

            public void bind(Video video) {
                video_number.setText(video.getVideo_number());
                video_name.setText(video.getVideo_name());
                video_content.setText(video.getVideo_content());
            }
        }
    }
}
