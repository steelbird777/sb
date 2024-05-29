package com.example.sb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        emailEditText = findViewById(R.id.email_edit_text);
        Button sendEmailButton = findViewById(R.id.send_email_button);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                sendEmail(email);
            }
        });
    }

    private void sendEmail(String email) {
        String subject = "New course material has arrived!";
        String body = "Join us now on the live YouTube video session at 6.00pm";
        EmailUtil.sendEmailInBackground(this, email, subject, body);
    }
}
