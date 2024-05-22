package com.example.weather.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Initialize your UI components
        CalendarView cv = findViewById(R.id.cv);
        TextView dateSentenceTextView = findViewById(R.id.dateSentenceTextView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        TextView voltageTextView = findViewById(R.id.voltageData);
        TextView temperatureTextView = findViewById(R.id.temperatureData);
        TextView humidityTextView = findViewById(R.id.humidityData);
        ImageView backBtn = findViewById(R.id.backbtn);
        RadioButton button1 = findViewById(R.id.button1);
        RadioButton button2 = findViewById(R.id.button2);
        RadioButton button3 = findViewById(R.id.button3);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.button1)
                {
                    DatabaseReference dailyDataRef = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
                    dailyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Check if the data snapshot exists and has children
                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                // Iterate through the children to retrieve data
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    // Extract data fields from the snapshot
                                    Double voltage = dataSnapshot.child("voltage").getValue(Double.class);

                                    // Retrieve temperature data (assuming it's stored as an integer)
                                    Integer temperature = dataSnapshot.child("temperature").getValue(Integer.class);

                                    // Retrieve humidity data (assuming it's stored as an integer)
                                    Integer humidity = dataSnapshot.child("humidity").getValue(Integer.class);

                                    // Do something with the retrieved data, like updating UI components
                                    updateUIWithData(voltage, temperature, humidity);
                                }
                            } else {
                                // Handle the case where no data is available
                                // For example, display a message to the user
                                Toast.makeText(getApplicationContext(), "No data available for button 1", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors, if any
                            Log.e("retrieveDataForButton1", "Database error: " + databaseError.getMessage());
                        }
                    });
                }
                else if (checkedId == R.id.button2)
                {

                } else if (checkedId == R.id.button3) {
                    // Handle button 3 selection
                } else {
                    // Handle default case
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(calendar.this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
                // Add any action you want to perform
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(calendar.this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
                // Add any action you want to perform
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(calendar.this, "Button 3 clicked", Toast.LENGTH_SHORT).show();
                // Add any action you want to perform
            }
        });

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Increment month by 1 because Calendar month is zero-based
                month++;
                String date = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (month < 10 ? "0" + month : month) + "/" + year;
                String selectedDateSentence = "Selected date: " + date  ;

                dateSentenceTextView.setText(selectedDateSentence);
            }
        });
    }



    private void updateUIWithData(Double voltage, Integer temperature, Integer humidity) {
        // Update UI components with the retrieved data
        TextView voltageTextView = findViewById(R.id.voltageData);
        TextView temperatureTextView = findViewById(R.id.temperatureData);
        TextView humidityTextView = findViewById(R.id.humidityData);

        // Assuming voltageTextView is your TextView
        voltageTextView.setText(String.valueOf(voltage));
        temperatureTextView.setText(String.valueOf(temperature));
        humidityTextView.setText(String.valueOf(humidity));
    }

}
