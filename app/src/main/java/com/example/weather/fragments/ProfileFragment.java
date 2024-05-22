package com.example.weather.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.weather.activities.EnergyUsage;
import com.example.weather.activities.QueryForm;
import com.example.weather.activities.feedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.R;
import com.example.weather.activities.Login;
import com.example.weather.activities.aboutus;
import com.example.weather.activities.privacynpolicy;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find the button view by its ID
        Button b1 = view.findViewById(R.id.support);
        Button b3 = view.findViewById(R.id.button3);
        Button apply = view.findViewById(R.id.appliances);
        Button b4 = view.findViewById(R.id.button4);
        Button b5 = view.findViewById(R.id.button5);
        Button b6 = view.findViewById(R.id.button6);
        Button b7 = view.findViewById(R.id.button7);
        Button b8 = view.findViewById(R.id.button8);

        TextView emailTextView = view.findViewById(R.id.currentuseremail);
        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, update UI with their email
            String email = currentUser.getEmail();
            emailTextView.setText(email);
        }
        else
        {
            // User is not signed in, handle this case if necessary
        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    Intent intent = new Intent(getContext(), QueryForm.class);
                    startActivity(intent);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), privacynpolicy.class);
                startActivity(intent);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EnergyUsage.class);
                startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), aboutus.class);
                startActivity(intent);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Replace "com.example.yourapp" with your app's package name
                String appPackageName = "com.example.weather";

                try {
                    // Open the Play Store app with your app's page
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    // If Play Store app is not available, open the Play Store website
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            private static final String PREF_NAME = "login_pref";
            private static final String KEY_REMEMBER_ME = "remember_me"; // Define KEY_REMEMBER_ME here

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Button Log Out is Clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set dialog title and message
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to proceed?");

                // Add "Yes" button and its functionality

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Initialize SharedPreferences
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

                        // Clear "Remember me" preference
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_REMEMBER_ME, false);
                        editor.apply();
                        // Handle "Yes" button click
                        // You can perform your action here
                        // Create an Intent to start the Login activity
                        Intent intent = new Intent(getContext(), Login.class);
                        // Clear the back stack so that user cannot navigate back to the MainActivity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Log Out", Toast.LENGTH_SHORT).show();
                    }
                });

                // Add "No" button and its functionality
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle "No" button click
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharedText = "Check out this link: https://example.com";

                // Create an Intent to share the text
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText);

                // Start the activity to share
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), feedback.class);
                    startActivity(intent);
                }
        });
    }

}