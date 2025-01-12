package com.example.easybooking.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.easybooking.R;
import com.example.easybooking.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;

    public ProfileFragment(){
        // require a empty public constructor
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
        Button logOutButton = view.findViewById(R.id.logOutButton);
        mAuth = FirebaseAuth.getInstance();

        logOutButton.setOnClickListener(v -> {
            // Sign out the user
            mAuth.signOut();

            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            getActivity().finish();
        });
    }
}