package com.example.weather.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;

public class passwordForgot extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailidl);
        resetButton = findViewById(R.id.submit);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Enter a valid email address");
                    return;
                }

                // Send password reset email
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(passwordForgot.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    finish(); // Finish activity after sending email
                                } else {
                                    Toast.makeText(passwordForgot.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}