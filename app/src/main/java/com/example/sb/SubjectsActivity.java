package com.example.sb;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    private ProgressBar progressBar;
    private Button enrollsBtn;

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
        subjectRecyclerView = findViewById(R.id.subjectRecyclerView);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchSubjects();
    }

    public void fetchSubjects() {
        db.collection("user_video")
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
        private Context mContext;

        public SubjectAdapter(List<Subject> subjects) {
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_card, parent, false);
            return new SubjectViewHolder(view);
        }

        // Inside the onBindViewHolder method of SubjectAdapter
        @Override
        public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
            holder.bind(subjects.get(position));

            holder.enrollsBtn.setOnClickListener(view -> {
                Subject subject = subjects.get(position);

                // Get user ID (you can get it from mAuth)
                String userId = ""; // Get the user ID here

                // Get current date (you can format it as needed)
                String enrollDate = ""; // Get the current date here

                // Insert data into the database
                EnrollsDbHelper dbHelper = new EnrollsDbHelper(view.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(EnrollsContract.EnrollmentEntry.COLUMN_NAME_USER_ID, userId);
                values.put(EnrollsContract.EnrollmentEntry.COLUMN_NAME_SUBJECT_ID, subject.getSubject_id());
                values.put(EnrollsContract.EnrollmentEntry.COLUMN_NAME_SUBJECT_NAME, subject.getSubject_name());
                values.put(EnrollsContract.EnrollmentEntry.COLUMN_NAME_ENROLL_DATE, enrollDate);

                long newRowId = db.insert(EnrollsContract.EnrollmentEntry.TABLE_NAME, null, values);
                if (newRowId != -1) {
                    Toast.makeText(view.getContext(), "Enrolled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Failed to enroll", Toast.LENGTH_SHORT).show();
                }
            });
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
                enrollsBtn = itemView.findViewById(R.id.enroll_button);
            }

            public void bind(Subject subject) {
                subject_name.setText(subject.getSubject_name());
                enrollsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Determine which subject was clicked
                        String subjectName = subject.getSubject_name();
                        if (subjectName.equals("Maths")) {
                            // Start MathsActivity
                            Context context = itemView.getContext();
                            Intent intent = new Intent(context, MathsActivity.class);
                            context.startActivity(intent);
                        } else if (subjectName.equals("Science")) {
                            // Start ScienceActivity
                            Context context = itemView.getContext();
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
