package com.example.easybooking.fragments;

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

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText firstNameEditText, lastNameEditText, phoneEditText;
    private ImageView firstNameEditButton, lastNameEditButton, phoneEditButton;
    private TextView usernameTextView;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        firstNameEditButton = view.findViewById(R.id.firstNameEditButton);
        lastNameEditButton = view.findViewById(R.id.lastNameEditButton);
        phoneEditButton = view.findViewById(R.id.phoneEditButton);
        usernameTextView = view.findViewById(R.id.profileNameTextView);
        TextView changePasswordTextView = view.findViewById(R.id.changePasswordTextView);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Handle First Name edit button click
        firstNameEditButton.setOnClickListener(v -> toggleEditMode(firstNameEditText, firstNameEditButton, "firstName"));

        // Handle Last Name edit button click
        lastNameEditButton.setOnClickListener(v -> toggleEditMode(lastNameEditText, lastNameEditButton, "lastName"));

        // Handle Phone edit button click
        phoneEditButton.setOnClickListener(v -> toggleEditMode(phoneEditText, phoneEditButton, "phone"));

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
                            // Set username, first name, last name, and phone
                            usernameTextView.setText(currentUser.getUsername());
                            firstNameEditText.setText(currentUser.getFirstName());
                            lastNameEditText.setText(currentUser.getLastName());
                            phoneEditText.setText(currentUser.getPhone());
                        }
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                });
    }

    // Function to toggle edit mode
    private void toggleEditMode(EditText editText, ImageView editButton, String field) {
        if (editText.isEnabled()) {
            // Get the new value entered by the user
            String newValue = editText.getText().toString().trim();

            // Check if the input is empty
            if (newValue.isEmpty()) {
                Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                return; // Do not save if the field is empty
            }

            // Save the new value to Firestore
            if (field.equals("firstName")) {
                currentUser.setFirstName(newValue);
            } else if (field.equals("lastName")) {
                currentUser.setLastName(newValue);
            } else if (field.equals("phone")) {
                currentUser.setPhone(newValue);
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
