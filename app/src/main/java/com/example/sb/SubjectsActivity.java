package com.example.sb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SubjectsActivity extends AppCompatActivity {
    private static final String TAG = "SubjectsActivity";
    private RecyclerView subjectRecyclerView;
    private FirebaseFirestore db;
    private SubjectAdapter subjectAdapter;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button enrollsBtn;
    private View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        subjectRecyclerView = findViewById(R.id.subjectRecyclerView);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fetchSubjects(userEmail);


        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SubjectsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void fetchSubjects(String userEmail) {
        db.collection("user_video")
                .whereEqualTo("user_mail",userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> subject_ids = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String subject_id = document.getString("subject_id");
                            subject_ids.add(subject_id);
                        }
                        if (subject_ids.isEmpty()) {
                            // Handle the case where no subject IDs are fetched
                            Log.e(TAG, "No subject IDs fetched");
                            Toast.makeText(SubjectsActivity.this, "No subjects available", Toast.LENGTH_SHORT).show();
                        } else {
                            fetchSubjectDetails(subject_ids);
                        }
                    } else {
                        Log.e(TAG, "Error fetching subjects: ", task.getException());
                        Toast.makeText(SubjectsActivity.this, "Error fetching subjects", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }


    public void fetchSubjectDetails(List<String> subject_ids) {
        if (subject_ids.isEmpty()) {
            Log.e(TAG, "No subject IDs to fetch details for");
            // You might want to handle this case, such as showing a message to the user
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        db.collection("user_video")
                .whereIn("subject_id", subject_ids)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Subject> subjects = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            // Retrieve subject details from the DocumentSnapshot
                            String subject_name = document.getString("subject_name");
                            String subject_id = document.getId();
                            Subject subject = new Subject(subject_id, subject_name);
                            subjects.add(subject);
                        }
                        updateRecyclerView(subjects);
                    } else {
                        Log.e(TAG, "Error fetching subject details: ", task.getException());
                        Toast.makeText(SubjectsActivity.this, "Error fetching subjects", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    public void updateRecyclerView(List<Subject> subjects) {
        subjectAdapter = new SubjectAdapter(subjects);
        subjectRecyclerView.setAdapter(subjectAdapter);
    }

    public static class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
        private List<Subject> subjects;

        public SubjectAdapter(List<Subject> subjects) {
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
            return new SubjectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
            holder.bind(subjects.get(position));
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        public static class SubjectViewHolder extends RecyclerView.ViewHolder {
            private TextView subject_name;
            private Button enrollsBtn;

            public SubjectViewHolder(@NonNull View itemView) {
                super(itemView);
                subject_name = itemView.findViewById(R.id.subject_name);
                enrollsBtn = itemView.findViewById(R.id.enrollBtn);
            }

            public void bind(Subject subject) {
                subject_name.setText(subject.getSubject_name());
                enrollsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Use itemView.getContext() instead of this
                        NotificationHelper.createNotificationChannel(itemView.getContext()); // Call this in onCreate or wherever appropriate
                        NotificationHelper.showNotification(itemView.getContext(), "Welcome message"); // Call this wherever you want to show a notification

                        // Determine which subject was clicked
                        String subjectName = subject.getSubject_name();
                        String activityName = subjectName + "Activity";
                        Context context = itemView.getContext();

                        try {
                            // Get the full class name by including the package name
                            Class<?> activityClass = Class.forName(context.getPackageName() + "." + activityName);
                            Intent intent = new Intent(context, activityClass);
                            context.startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            // Handle the exception (e.g., show a toast or log an error)
                            // For example, you can log the error or show a message to the user
                            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

        }

    }
}
