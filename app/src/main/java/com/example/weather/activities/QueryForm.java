package com.example.weather.activities;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weather.R;

public class QueryForm extends AppCompatActivity {
    private EditText editTextQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_query_form);
        editTextQuery = findViewById(R.id.editTextQuery);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        ImageView backBtn = findViewById(R.id.backbtn);

        // Set an OnClickListener on the ImageView
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextQuery.getText().toString().trim();
                if (!query.isEmpty()) {
                    sendEmail(query);
                } else {
                    Toast.makeText(QueryForm.this, "Please enter your query", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(String query) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "User Query");
        intent.putExtra(Intent.EXTRA_TEXT, query);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }

}