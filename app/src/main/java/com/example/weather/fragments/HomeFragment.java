package com.example.weather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather.LineChartView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.weather.R;
import com.example.weather.activities.calendar;
import com.example.weather.activities.wheather;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weather.activities.solar_benifits;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View rootViewview, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        ImageView benifit = rootView.findViewById(R.id.solarbenifits);
        RadioGroup radioGroup = rootView.findViewById(R.id.radioGroup);
        RadioButton button1 = rootView.findViewById(R.id.button1);
        RadioButton button2 = rootView.findViewById(R.id.button2);
        RadioButton button3 = rootView.findViewById(R.id.button3);
        CalendarView cv = rootView.findViewById(R.id.cv);
        TextView dateSentenceTextView = rootView.findViewById(R.id.dateSentenceTextView);
        //LineChartView lineChartView = rootView.findViewById(R.id.lineChartView);

        List<Float> dataPoints = new ArrayList<>();
        dataPoints.add(100f); // Add your voltage data points here
        dataPoints.add(150f);
        dataPoints.add(200f);
        dataPoints.add(250f);

       // lineChartView.setData(dataPoints);

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(getContext());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Check for angle value
                    if (dataSnapshot.hasChild("angle")) {
                        Integer angleValue = dataSnapshot.child("angle").getValue(Integer.class);
                        updateAngleTextView(angleValue);
                    }
                    // Check for voltage value
                    if (dataSnapshot.hasChild("voltage")) {
                        Double voltageValue = dataSnapshot.child("voltage").getValue(Double.class);
                        updateVoltageTextView(voltageValue);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error without logging
                Toast.makeText(getContext(), "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "No data available for button 1", Toast.LENGTH_SHORT).show();

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
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Increment month by 1 because Calendar month is zero-based
                month++;
                String date = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (month < 10 ? "0" + month : month) + "/" + year;
                String selectedDateSentence = "Selected date: " + date  ;

                String selectedDayStartTimestamp = year + "-" + (month < 10 ? "0" + month : month) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " 00:00:00";
                String selectedDayEndTimestamp = year + "-" + (month < 10 ? "0" + month : month) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " 23:59:59";

                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("User1").child("Date");
                Query query = dataref.orderByChild("timestamp").startAt(selectedDayStartTimestamp).endAt(selectedDayEndTimestamp);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Your code to handle data retrieval
                        int overallVoltage = 0; // Initialize overall voltage accumulator
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Assuming each child node has a "voltage" field
                            int voltage = snapshot.child("voltage").getValue(Integer.class);
                            Log.d("Voltage", "Voltage retrieved: " + voltage);
                            overallVoltage += voltage; // Accumulate voltage values
                        }

                        // Update UI to display the overall voltage
                        Log.d("OverallVoltage", "Overall voltage: " + overallVoltage);
                        TextView voltageData=rootView.findViewById(R.id.voltageData);
                        voltageData.setText("" + overallVoltage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                        // Handle errors
                    }
                });
                dateSentenceTextView.setText(selectedDateSentence);
            }
        });


        benifit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event of the ImageView here
                Intent intent = new Intent(getActivity(), solar_benifits.class);
                intent.putExtra("identifier", 123); // Replace yourIdentifier with actual value
                startActivity(intent);
            }
        });
    }

    private void updateUIWithData(Double voltage, Integer temperature, Integer humidity) {
        // Update UI components with the retrieved data
        TextView voltageTextView = rootView.findViewById(R.id.voltageData);
        TextView temperatureTextView = rootView.findViewById(R.id.temperatureData);
        TextView humidityTextView = rootView.findViewById(R.id.humidityData);

        // Assuming voltageTextView is your TextView
        voltageTextView.setText(String.valueOf(voltage));
        temperatureTextView.setText(String.valueOf(temperature));
        humidityTextView.setText(String.valueOf(humidity));
    }

    private void updateVoltageTextView(Double voltageValue) {
        TextView voltageTextView = rootView.findViewById(R.id.voltageFetch);
        // Assuming voltageFetch is the ID of your voltage TextView
        if (voltageTextView != null && voltageValue != null) {
            voltageTextView.setText(String.valueOf(voltageValue) + " V");
        }
    }


    private void updateAngleTextView(Integer angleValue) {
        TextView angleTextView = rootView.findViewById(R.id.angleFetch);
        // Assuming angleFetch is the ID of your angle TextView
        if (angleTextView != null && angleValue != null) {
            angleTextView.setText(String.valueOf(angleValue) + "°");
        }
    }

}