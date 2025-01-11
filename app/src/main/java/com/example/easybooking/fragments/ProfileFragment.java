package com.example.easybooking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easybooking.R;

public class ProfileFragment extends Fragment {

    private EditText emailEditText, phoneEditText;
    private ImageView emailEditButton, phoneEditButton;
    private TextView changePasswordTextView;
    private Button logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditButton = view.findViewById(R.id.emailEditButton);
        phoneEditButton = view.findViewById(R.id.phoneEditButton);
        changePasswordTextView = view.findViewById(R.id.changePasswordTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Handle email edit button click
        emailEditButton.setOnClickListener(v -> toggleEditMode(emailEditText, emailEditButton));

        // Handle phone edit button click
        phoneEditButton.setOnClickListener(v -> toggleEditMode(phoneEditText, phoneEditButton));

        // Handle Change Password click
        changePasswordTextView.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ChangePasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            // Handle logout functionality
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Function to toggle edit mode
    private void toggleEditMode(EditText editText, ImageView editButton) {
        if (editText.isEnabled()) {
            // Save the new value
            editText.setEnabled(false);
            editButton.setImageResource(R.drawable.ic_edit); // Change icon back to Edit
            Toast.makeText(getContext(), "Information saved", Toast.LENGTH_SHORT).show();
        } else {
            // Enable the EditText for editing
            editText.setEnabled(true);
            editButton.setImageResource(R.drawable.ic_save); // Change icon to Save
        }
    }
}
