package com.example.sb;

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
        Button backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> {
            Toast.makeText(SubjectsActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SubjectsActivity.this, MainActivity.class);
            startActivity(intent);
        });
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        subjectRecyclerView = findViewById(R.id.subjectRecyclerView);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fetchSubjects(userEmail);
    }
    public void fetchSubjects(String userEmail) {
        db.collection("subject")
                .whereEqualTo("user_mail", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> subject_ids = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String subject_id= document.getString("subject_id");
                            subject_ids.add(subject_id);
                        }
                        fetchSubjectDetails(subject_ids);
                    } else {
                        Log.e(TAG, "Error fetching subjects: ", task.getException());
                        Toast.makeText(SubjectsActivity.this, "Error fetching subjects", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }
    public void fetchSubjectDetails(List<String> subject_ids) {
        progressBar.setVisibility(View.INVISIBLE);
        db.collection("subject").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Subject> subjects = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Subject subject = document.toObject(Subject.class);
                            if (subject != null) {
                                subjects.add(subject);
                            }
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
        subjectAdapter = new SubjectsActivity.SubjectAdapter(subjects);
        subjectRecyclerView.setAdapter(subjectAdapter);
    }
    public static class SubjectAdapter extends RecyclerView.Adapter<SubjectsActivity.SubjectAdapter.SubjectViewHolder> {
        private List<Subject> subjects;

        public SubjectAdapter(List<Subject> subjects) {
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public SubjectsActivity.SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
            return new SubjectsActivity.SubjectAdapter.SubjectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectsActivity.SubjectAdapter.SubjectViewHolder holder, int position) {
            holder.bind(subjects.get(position));
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        public static class SubjectViewHolder extends RecyclerView.ViewHolder {
            private TextView subject_name;
            private TextView subject_id;


            public SubjectViewHolder(@NonNull View itemView) {
                super(itemView);
                subject_name = itemView.findViewById(R.id.subject_name);
            }

            public void bind(Subject subject) {
                subject_name.setText(subject.getSubject_name());
            }
        }
    }
}