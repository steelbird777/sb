package com.example.sb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText fname;
    private EditText email;
    private EditText mobile;
    private EditText password;
    private EditText cpassword;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private TextView login;
    private FirebaseFirestore firestore;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        fname = findViewById(R.id.fname);
        email = findViewById(R.id.semail);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.spassword);
        cpassword = findViewById(R.id.scpassword);
        signUpBtn = findViewById(R.id.signUpbtn);
        login = findViewById(R.id.slogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfname = fname.getText().toString().trim();
                String memail = email.getText().toString().trim();
                String mmobile = mobile.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                String mcpassword = cpassword.getText().toString().trim();



                if (memail.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mpassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mpassword.equals(mcpassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                int mob = Integer.parseInt(mobile.getText().toString());
                if (mob < 1000000000 || mob > 9999999999L) {
                    Toast.makeText(SignupActivity.this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user_id = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(user_id);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname", mfname);  // Correctly put fname string
                            user.put("user_mail", memail);  // Correctly put email string
                            user.put("mobile", mmobile);  // Correctly put mobile string

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignupActivity.this, "Inserted into Collection", Toast.LENGTH_SHORT).show();
                                }
                            });
                            DatabaseHelper dbHelper = new DatabaseHelper(SignupActivity.this);
                            dbHelper.addDetails(mfname,memail,mmobile);

                            fname.setText("");
                            email.setText("");
                            mobile.setText("");
                            password.setText("");
                            cpassword.setText("");
                            Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
