package com.example.sb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;
    private Button loginbtn;
    private EditText memail;
    private EditText mpassword;
    private ProgressBar mprogressBar;
    private Button adminbtn;
    private View closeBtn;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.loginButton);
        memail = findViewById(R.id.emailEditText);
        mpassword = findViewById(R.id.passwordEditText);
        mprogressBar = findViewById(R.id.progressBar);
        closeBtn = findViewById(R.id.closebtn);
        closeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finishAffinity();
                    }
                }
        );
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                mprogressBar.setVisibility(View.VISIBLE);
                loginbtn.setEnabled(false);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                mprogressBar.setVisibility(View.INVISIBLE);
                                loginbtn.setEnabled(true);
                                if (task.isSuccessful()) {
                                    memail.setText("");
                                    mpassword.setText("");
                                    Toast.makeText(MainActivity.this,"Authentication Success",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Retrieve FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }
}



