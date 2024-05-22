package com.example.weather.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.R;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class wheather extends AppCompatActivity {

    private DatabaseReference temper;
    private DatabaseReference humid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheather);

        TextView datetextView = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateAndTime = dateFormat.format(calendar.getTime());
        datetextView.setText(currentDateAndTime);
        ImageView backBtn = findViewById(R.id.backbtn);

        TextView temp = findViewById(R.id.degree);
        TextView humidity = findViewById(R.id.humidity);
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);
        ImageView sun1=findViewById(R.id.s1);
        ImageView sun2=findViewById(R.id.s2);
        ImageView sun3=findViewById(R.id.s3);
        ImageView sun4=findViewById(R.id.s4);
        ImageView sun5=findViewById(R.id.s5);

        // Create a RotateAnimation
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000); // Set the duration of the animation in milliseconds
        rotateAnimation.setRepeatCount(Animation.INFINITE); // Set the repeat count for the animation
        rotateAnimation.setInterpolator(new LinearInterpolator()); // Set the interpolator for smooth animation

        sun1.startAnimation(rotateAnimation);
        sun2.startAnimation(rotateAnimation);
        sun3.startAnimation(rotateAnimation);
        sun4.startAnimation(rotateAnimation);
        sun5.startAnimation(rotateAnimation);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });

        temper = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
        humid = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");

        temper.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.getKey().equals("temperature")) {
                    Double temperatureValue = dataSnapshot.getValue(Double.class);
                    updateTextView(dataSnapshot, temp);
                }
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.getKey().equals("temperature")) {
                    Double temperatureValue = dataSnapshot.getValue(Double.class);
                    updateTextView(dataSnapshot, temp);
                }

            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                // Handle if a child is removed
            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Handle if a child changes position
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error without logging
                Toast.makeText(wheather.this, "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        humid.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.getKey().equals("humidity")) {
                    Double humidValue = dataSnapshot.getValue(Double.class);
                    updateTextView1(dataSnapshot, humidity);
                }
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.getKey().equals("humidity")) {
                    Double humidValue = dataSnapshot.getValue(Double.class);
                    updateTextView1(dataSnapshot, humidity);
                }

            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                // Handle if a child is removed
            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Handle if a child changes position
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error without logging
                Toast.makeText(wheather.this, "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTextView1(DataSnapshot dataSnapshot, TextView humidity) {
        Integer integerValue = dataSnapshot.getValue(Integer.class);
        if (integerValue != null) {
            String humidityString = integerValue + "%";
            humidity.setText(humidityString);
        }
    }


    private void updateTextView(DataSnapshot dataSnapshot, TextView temp) {
        Integer integerValue = dataSnapshot.getValue(Integer.class);
        if (integerValue != null) {
            String temperatureString = integerValue + "°";
            temp.setText(temperatureString);
        }
    }


}