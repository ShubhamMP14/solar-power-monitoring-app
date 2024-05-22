package com.example.weather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.CheckBox;

import com.example.weather.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.weather.R;

public class Login extends AppCompatActivity {

    private static final String PREF_NAME = "login_pref";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private SharedPreferences sharedPreferences;
    private CheckBox rememberMeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        EditText loginEmail, loginPassword;
        Button loginButton;
        FirebaseAuth mAuth;

        loginEmail = findViewById(R.id.emailidl);
        loginPassword = findViewById(R.id.pass);

        // Find views
        rememberMeCheckbox = findViewById(R.id.rem);
        loginButton = findViewById(R.id.loginbt);
        TextView forgotpass=findViewById(R.id.forgotpass);
        TextView privacy=findViewById(R.id.privacy);

        mAuth = FirebaseAuth.getInstance();


        TextView pass=findViewById(R.id.pass);
        ImageView eye=findViewById(R.id.eye1);

        // Check if "Remember me" was previously selected
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (rememberMe) {
            // If "Remember me" was selected, go to home screen
            goToMainActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email cannot be empty");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginEmail.setError("Invalid email address");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password cannot be empty");
                    return;
                }

                if (password.length() < 6) {
                    loginPassword.setError("Password must be at least 6 characters long");
                    return;
                }
                if (!Character.isUpperCase(password.charAt(0))) {
                    loginPassword.setError("Password must start with a capital letter");
                    return;
                }

                if (!password.contains("@")) {
                    loginPassword.setError("Password must contain at least one '@' symbol");
                    return;
                }
                // Perform login using FirebaseAuth
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login successful, redirect to HomeFragment
                                    // Check if "Remember me" is selected
                                    boolean rememberMeChecked = rememberMeCheckbox.isChecked();
                                    if (rememberMeChecked) {
                                        // If selected, save the preference
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(KEY_REMEMBER_ME, true);
                                        editor.apply();
                                    }
                                    // Redirect to MainActivity
                                    goToMainActivity();
                                } else {
                                    // Login failed
                                    Toast.makeText(Login.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, privacynpolicy.class);
                startActivity(intent);
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });


        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, passwordForgot.class);
                startActivity(intent);
            }
        });
    }
    private void goToMainActivity() {
        // Launch main activity
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
        finish(); // Finish this login activity so user can't come back to it with back button
    }


    private void togglePasswordVisibility() {
        EditText pass = findViewById(R.id.pass);
        ImageView eye1 = findViewById(R.id.eye1);

        // Store the current cursor position
        int selection = pass.getSelectionEnd();

        if (pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // If the password is visible, make it hidden
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eye1.setImageResource(R.drawable.pass_icon); // Change the eye icon accordingly
        } else {
            // If the password is hidden, make it visible
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eye1.setImageResource(R.drawable.baseline_visibility_off_24); // Change the eye icon accordingly
        }

        // Set the selection to the stored cursor position
        pass.setSelection(selection);
    }
}

