package com.example.easybooking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easybooking.R;

public class ProfileFragment extends Fragment {

    private LinearLayout personalInfoLayout;
    private EditText emailEditText;
    private EditText phoneEditText;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI Components
        Button personalInfoButton = view.findViewById(R.id.personalInfoButton);
        personalInfoLayout = view.findViewById(R.id.personalInfoLayout);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Show/Hide Personal Information Section
        personalInfoButton.setOnClickListener(v -> {
            if (personalInfoLayout.getVisibility() == View.GONE) {
                personalInfoLayout.setVisibility(View.VISIBLE);
            } else {
                personalInfoLayout.setVisibility(View.GONE);
            }
        });

        // Save Button Click Listener
        saveButton.setOnClickListener(v -> {
            // Get updated information
            String updatedEmail = emailEditText.getText().toString();
            String updatedPhone = phoneEditText.getText().toString();

            // For now, just print the updated information
            // You can save it to SharedPreferences or a database
            System.out.println("Updated Email: " + updatedEmail);
            System.out.println("Updated Phone: " + updatedPhone);

            // Hide the Personal Information section
            personalInfoLayout.setVisibility(View.GONE);
        });
    }
}
