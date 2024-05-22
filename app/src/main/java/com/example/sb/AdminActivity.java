package com.example.sb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class AdminActivity extends AppCompatActivity {

    private MaterialCardView sendEmailCard;
    private MaterialCardView sendMessageCard;
    private MaterialCardView sendNotificationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sendEmailCard = findViewById(R.id.send_email_card);
        sendMessageCard = findViewById(R.id.send_message_card);
        sendNotificationCard = findViewById(R.id.send_notification_card);

        sendEmailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        sendMessageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, SmsActivity.class);
                startActivity(intent);
            }
        });

        sendNotificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call this in onCreate or wherever appropriate
                NotificationHelper.showNotification(AdminActivity.this,"Click Here!!!"); // Call this wherever you want to show a notification

            }
        });

        // Logout button functionality (if needed)
        Button logoutButton = findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logout logic here
                finish();
            }
        });
    }


}
