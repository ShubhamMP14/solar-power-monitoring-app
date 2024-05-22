package com.example.weather.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.weather.R;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Activity view;

    public DashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashFragment newInstance(String param1, String param2) {
        DashFragment fragment = new DashFragment();
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
    private DatabaseReference temper;
    private DatabaseReference humid;
    private TextView[] tmpTextViews;
    private TextView[] textViews;
    private SimpleDateFormat dateFormat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dash, container, false);
        initializeTextViews(rootView);
        // Inside a fragment or activity method where you want to fetch weather data
        String cityName = "Mumbai"; // Replace "London" with the desired city name
        new FetchWeatherTask().execute(cityName);
        // Find the ImageView by its id
        TextView temp = rootView.findViewById(R.id.degree);
        TextView humidity = rootView.findViewById(R.id.humidity);
        TextView datetextView = rootView.findViewById(R.id.date);
        TextView highest=rootView.findViewById(R.id.High);
        TextView Lowest=rootView.findViewById(R.id.Low);
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(getContext());
        temper = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
        humid = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
        //for next 5 days temperature
        tmpTextViews = new TextView[5];
        tmpTextViews[0] = rootView.findViewById(R.id.tmp1);
        tmpTextViews[1] = rootView.findViewById(R.id.tmp2);
        tmpTextViews[2] = rootView.findViewById(R.id.tmp3);
        tmpTextViews[3] = rootView.findViewById(R.id.tmp4);
        tmpTextViews[4] = rootView.findViewById(R.id.tmp5);

        ImageView sun1=rootView.findViewById(R.id.s1);
        ImageView sun2=rootView.findViewById(R.id.s2);
        ImageView sun3=rootView.findViewById(R.id.s3);
        ImageView sun4=rootView.findViewById(R.id.s4);
        ImageView sun5=rootView.findViewById(R.id.s5);
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int monthIndex = calendar.get(Calendar.MONTH);
        String month = months[monthIndex];
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String formattedMinute = String.format("%02d", minute);

        String url="api.openweathermap.org/data/2.5/forecast?zip={zip code},{country code}&appid={API key}";
        String apiKey="46fa13be9a9779abf57b9b53b88ee61b";


        String formattedDate = dayOfMonth + " " + month + " " + year + " " + hour + ":" + formattedMinute;
        datetextView.setText(formattedDate);


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


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User1").child("Realtime");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Check for angle value
                    if (dataSnapshot.hasChild("angle")) {
                        Integer angleValue = dataSnapshot.child("angle").getValue(Integer.class);
                        updateAngleTextView(angleValue,rootView);
                    }
                    // Check for voltage value
                    if (dataSnapshot.hasChild("voltage")) {
                        Double voltageValue = dataSnapshot.child("voltage").getValue(Double.class);
                        updateVoltageTextView(voltageValue,rootView);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error without logging
                Toast.makeText(getContext(), "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(getContext(), "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Failed to read value. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        // Return the inflated view
        return rootView;
    }
    //start
    public class FetchWeatherTask extends AsyncTask<String, Void, String> {

        private static final String API_KEY = "64193e739aae2c506c089bda0b5bad5c";
        private static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast";

        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0]; // Assuming you pass the city name as the parameter

            try {
                // Construct the URL with the city name and API key
                String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
                String urlString = API_URL + "?q=" + encodedCityName + "&appid=" + API_KEY;

                // Log the URL being used for the API request
                Log.d("FetchWeatherTask", "Fetching weather data from URL: " + urlString);

                // Create a URL object and open connection
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set up connection properties
                connection.setRequestMethod("GET");

                // Get the response stream
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                // Read the response line by line
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                // Close the streams
                bufferedReader.close();
                inputStream.close();

                // Return the JSON response as a string
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                // Log error message
                Log.e("FetchWeatherTask", "Error fetching weather data: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    // Parse JSON response and process data
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray weatherList = jsonResponse.getJSONArray("list");

                    // Get today's date
                    Calendar todayCalendar = Calendar.getInstance();
                    int todayDayOfYear = todayCalendar.get(Calendar.DAY_OF_YEAR);
                    int todayYear = todayCalendar.get(Calendar.YEAR);

                    int highestTemp = Integer.MIN_VALUE;
                    int lowestTemp = Integer.MAX_VALUE;

                    // Update TextViews with temperature data for each day
                    for (int i = 0; i < 5; i++) {
                        JSONObject weatherData = weatherList.getJSONObject(i);
                        JSONObject main = weatherData.getJSONObject("main");
                        double temperatureKelvin = main.getDouble("temp");
                        double temperatureCelsius = temperatureKelvin - 273.15;



                        // Update the corresponding TextView with the temperature value without decimal points
                        tmpTextViews[i].setText(String.format(Locale.getDefault(), "%.0f °C", temperatureCelsius));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Log error message
                    Log.e("FetchWeatherTask", "Error parsing JSON response: " + e.getMessage());
                }
            } else {
                // Log error message
                Log.e("FetchWeatherTask", "Weather data fetch failed: Result is null");
            }
            // Show a Toast message indicating whether data fetch was successful
            String toastMessage = (result != null) ? "Weather data fetched successfully" : "Failed to fetch weather data";
            Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }


    }

    //end

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

    private void updateVoltageTextView(Double voltageValue, View rootView) {
        TextView voltageTextView = rootView.findViewById(R.id.voltageFetch);
        // Assuming voltageFetch is the ID of your voltage TextView
        if (voltageTextView != null && voltageValue != null) {
            voltageTextView.setText(String.valueOf(voltageValue));
        }
    }

    private void updateAngleTextView(Integer angleValue, View rootView) {
        TextView angleTextView = rootView.findViewById(R.id.angleFetch);
        // Assuming angleFetch is the ID of your angle TextView
        if (angleTextView != null && angleValue != null) {
            angleTextView.setText(String.valueOf(angleValue));
        }
    }
    public void initializeTextViews(View rootView) {
        textViews = new TextView[5];
        textViews[0] = rootView.findViewById(R.id.D1);
        textViews[1] = rootView.findViewById(R.id.D2);
        textViews[2] = rootView.findViewById(R.id.D3);
        textViews[3] = rootView.findViewById(R.id.D4);
        textViews[4] = rootView.findViewById(R.id.D5);

        dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());

        // Get the day after tomorrow's date
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_YEAR, 1); // Add 2 days to get the day after tomorrow
        Date dayAfterTomorrow = calendar1.getTime();

        // Set text for D1 (day after tomorrow)
        textViews[0].setText(dateFormat.format(dayAfterTomorrow));

        // Set text for D2, D3, D4, D5 (increment by one day)
        for (int i = 1; i < textViews.length; i++) {
            calendar1.add(Calendar.DAY_OF_YEAR, 1);
            Date nextDay = calendar1.getTime();
            textViews[i].setText(dateFormat.format(nextDay));
        }
    }

}