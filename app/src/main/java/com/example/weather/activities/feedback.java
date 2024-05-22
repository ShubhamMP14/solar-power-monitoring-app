package com.example.weather.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.weather.R;

public class feedback extends AppCompatActivity {

    private EditText feedbackEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        feedbackEditText = findViewById(R.id.ETFeedback);
        Button submit=findViewById(R.id.feedsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String feedbackMessage = feedbackEditText.getText().toString().trim();

        if (feedbackMessage.isEmpty()) {
            // Handle empty feedback message
            return;
        }
        String subject = "Feedback from Weather App";
        String recipientEmail = "sam.bhute20@gmail.com"; // Replace with developer's email address
        // Create an Intent to send an email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recipientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedbackMessage);

        // Check if there's an email client available to handle the intent
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            // Handle if no email client is available
            Toast.makeText(this, "No email app found on your device. Please visit our website or contact support directly.", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = getBuilder();

            builder.show();
        }
    }

    @androidx.annotation.NonNull
    private AlertDialog.Builder getBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Email App Found");
        builder.setMessage("No email app found on your device. Please visit our website or contact support directly.");

        builder.setPositiveButton("Visit Website", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Open the website in a browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://solarapp.com"));
                startActivity(browserIntent);
            }
        });
        builder.setNegativeButton("Contact Support", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open email client with support email address pre-filled
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:sam.bhute20@gmail.com"));
                startActivity(emailIntent);
            }
        });
        return builder;
    }

}