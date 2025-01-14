package com.example.easybooking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easybooking.R;

public class ChangePasswordFragment extends Fragment {

    private EditText currentPasswordEditText, newPasswordEditText;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Initialize UI components
        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        Button savePasswordButton = view.findViewById(R.id.savePasswordButton);

        // Handle Save Password button click
        savePasswordButton.setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle password update logic here
                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
