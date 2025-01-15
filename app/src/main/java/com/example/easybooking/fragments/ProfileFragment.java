package com.example.easybooking.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.easybooking.activities.LoginActivity;
import com.example.easybooking.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText usernameEditText, firstNameEditText, lastNameEditText, phoneEditText, dobEditText;
    private ImageView usernameEditButton, firstNameEditButton, lastNameEditButton, phoneEditButton, dobEditButton;
    private TextView usernameTextView;
    private User currentUser;

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

        // Handle edit buttons
        usernameEditButton.setOnClickListener(v -> toggleEditMode(usernameEditText, usernameEditButton, "username"));
        firstNameEditButton.setOnClickListener(v -> toggleEditMode(firstNameEditText, firstNameEditButton, "firstName"));
        lastNameEditButton.setOnClickListener(v -> toggleEditMode(lastNameEditText, lastNameEditButton, "lastName"));
        phoneEditButton.setOnClickListener(v -> toggleEditMode(phoneEditText, phoneEditButton, "phone"));
        dobEditButton.setOnClickListener(v -> toggleEditMode(dobEditText, dobEditButton, "dob"));

        dobEditText.setFocusable(false);
        dobEditText.setClickable(true);
        dobEditText.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        // Handle Change Password click
        changePasswordTextView.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ChangePasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            // Sign out the user
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        // Load user profile data
        loadUserProfile();

        return view;
    }

    // Function to load user profile data from Firestore
    private void loadUserProfile() {
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
                        }
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
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
            // Get the new value entered by the user
            String newValue = editText.getText().toString().trim();

            if (newValue.isEmpty()) {
                Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the new value to Firestore
            if (field.equals("firstName")) {
                currentUser.setFirstName(newValue);
            } else if (field.equals("lastName")) {
                currentUser.setLastName(newValue);
            } else if (field.equals("phone")) {
                currentUser.setPhone(newValue);
            } else if (field.equals("dob")) {
                currentUser.setDateOfBirth(newValue);
            } else if (field.equals("username")) {
                currentUser.setUsername(newValue);
            }

            // Update the Firestore document with new values
            firestore.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        // Disable the EditText after saving the changes
                        editText.setEnabled(false);
                        editButton.setImageResource(R.drawable.ic_edit); // Change icon back to Edit
                        Toast.makeText(getContext(), "Information saved", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save information", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Enable the EditText for editing
            editText.setEnabled(true);
            editButton.setImageResource(R.drawable.ic_save); // Change icon to Save
        }
    }

}
