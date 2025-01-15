package com.example.easybooking.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.easybooking.AdminActivity;
import com.example.easybooking.R;
import com.example.easybooking.ViewUserActivity;
import com.example.easybooking.activities.LoginActivity;
import com.example.easybooking.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText usernameEditText, firstNameEditText, lastNameEditText, phoneEditText, dobEditText;
    private ImageView usernameEditButton, firstNameEditButton, lastNameEditButton, phoneEditButton, dobEditButton;
    private TextView usernameTextView;
    private User currentUser;
    private Button viewUserLayoutButton;
    private Button backToAdminButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        usernameEditText = view.findViewById(R.id.usernameEditText);
        usernameEditButton = view.findViewById(R.id.usernameEditButton);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        firstNameEditButton = view.findViewById(R.id.firstNameEditButton);
        lastNameEditButton = view.findViewById(R.id.lastNameEditButton);
        phoneEditButton = view.findViewById(R.id.phoneEditButton);
        usernameTextView = view.findViewById(R.id.profileNameTextView);
        dobEditText = view.findViewById(R.id.dobEditText);
        dobEditButton = view.findViewById(R.id.dobEditButton);
        TextView changePasswordTextView = view.findViewById(R.id.changePasswordTextView);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Initialize the admin buttons
        viewUserLayoutButton = view.findViewById(R.id.viewUserLayoutButton);
        backToAdminButton = view.findViewById(R.id.backToAdminButton);

        // Hide buttons by default
        viewUserLayoutButton.setVisibility(View.GONE);
        backToAdminButton.setVisibility(View.GONE);

        // Handle edit buttons
        usernameEditButton.setOnClickListener(v -> toggleEditMode(usernameEditText, usernameEditButton, "username"));
        firstNameEditButton.setOnClickListener(v -> toggleEditMode(firstNameEditText, firstNameEditButton, "firstName"));
        lastNameEditButton.setOnClickListener(v -> toggleEditMode(lastNameEditText, lastNameEditButton, "lastName"));
        phoneEditButton.setOnClickListener(v -> toggleEditMode(phoneEditText, phoneEditButton, "phone"));
        dobEditButton.setOnClickListener(v -> toggleEditMode(dobEditText, dobEditButton, "dob"));

        dobEditText.setFocusable(false);
        dobEditText.setClickable(true);
        dobEditText.setOnClickListener(this::showDatePickerDialog);

        // Handle Change Password click
        changePasswordTextView.setOnClickListener(this::handleChangePassword);

        // Handle logout button click
        logoutButton.setOnClickListener(this::handleLogout);

        // Load user profile data
        loadUserProfile();

        viewUserLayoutButton.setOnClickListener(this::handleViewUserLayout);
        backToAdminButton.setOnClickListener(this::handleBackToAdmin);

        return view;
    }

    // Function to load user profile data from FireStore
    private void loadUserProfile() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null) {
                            usernameTextView.setText(currentUser.getEmail());
                            usernameEditText.setText(currentUser.getUsername());
                            firstNameEditText.setText(currentUser.getFirstName());
                            lastNameEditText.setText(currentUser.getLastName());
                            phoneEditText.setText(currentUser.getPhone());
                            dobEditText.setText(currentUser.getDateOfBirth());

                            // Check user role and show/hide admin buttons
                            String userRole = currentUser.getRole();
                            if (userRole != null && userRole.equals("admin")) {
                                viewUserLayoutButton.setVisibility(View.VISIBLE);
                                backToAdminButton.setVisibility(View.VISIBLE);
                            } else {
                                viewUserLayoutButton.setVisibility(View.GONE);
                                backToAdminButton.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this::handleProfileLoadFailure);
    }

    // Add this new method to handle the failure
    private void handleProfileLoadFailure(Exception e) {
        Toast.makeText(getContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", 
                        dayOfMonth, month + 1, year);
                    dobEditText.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // Function to toggle edit mode
    private void toggleEditMode(EditText editText, ImageView editButton, String field) {
        if (editText.isEnabled()) {
            String newValue = editText.getText().toString().trim();

            if (newValue.isEmpty()) {
                Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (field) {
                case "firstName":
                    currentUser.setFirstName(newValue);
                    break;
                case "lastName":
                    currentUser.setLastName(newValue);
                    break;
                case "phone":
                    currentUser.setPhone(newValue);
                    break;
                case "dob":
                    currentUser.setDateOfBirth(newValue);
                    break;
                case "username":
                    currentUser.setUsername(newValue);
                    break;
            }

            // Update FireStore
            if (mAuth.getCurrentUser() != null) {
                firestore.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .set(currentUser)
                        .addOnSuccessListener(aVoid -> {
                            editText.setEnabled(false);
                            editButton.setImageResource(R.drawable.ic_edit);
                            Toast.makeText(getContext(), "Information saved", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> 
                            Toast.makeText(getContext(), "Failed to save information", Toast.LENGTH_SHORT).show()
                        );
            }
        } else {
            editText.setEnabled(true);
            editButton.setImageResource(R.drawable.ic_save);
        }
    }

    private void handleViewUserLayout(View v) {
        try {
            // Navigate to ViewUserActivity
            Intent intent = new Intent(getActivity(), ViewUserActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            // Log the error
            Log.e("ProfileFragment", "Error navigating to ViewUserActivity: " + e.getMessage());
            // Show error message to user
            Toast.makeText(getContext(), 
                "Failed to open user view: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void handleBackToAdmin(View v) {
        try {
            // Navigate to AdminActivity
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
            requireActivity().finish(); // Close the current activity
        } catch (Exception e) {
            // Log the error
            Log.e("ProfileFragment", "Error navigating to AdminActivity: " + e.getMessage());
            // Show error message to user
            Toast.makeText(getContext(), 
                "Failed to return to admin: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogout(View v) {
        // Sign out the user
        mAuth.signOut();
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        // Navigate to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void handleChangePassword(View v) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ChangePasswordFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
