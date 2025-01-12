package com.example.easybooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easybooking.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, navigate to the appropriate activity
            fetchUserRoleAndNavigate(currentUser.getUid());
        }  else {
            // User is not logged in, navigate to LoginActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // This function will fetch the user's role from Firestore and navigate to the appropriate activity
    private void fetchUserRoleAndNavigate(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");

                        if ("admin".equals(role)) {
                            navigateToAdminActivity();
                        } else {
                            navigateToMainActivity();
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToAdminActivity() {
        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}