package com.example.easybooking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easybooking.fragments.CarFragment;
import com.example.easybooking.fragments.HotelFragment;
import com.example.easybooking.fragments.ProfileFragment;
import com.example.easybooking.fragments.TransportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class ViewUserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_user_activity);

        FirebaseApp.initializeApp(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.hotel2) {
                replaceFragment(new HotelFragment());
            } else if (itemId == R.id.car2) {
                replaceFragment(new CarFragment());
            } else if (itemId == R.id.transport2) {
                replaceFragment(new TransportFragment());
            }
             else if (itemId == R.id.profile2) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
        replaceFragment(new HotelFragment()); // Set initial fragment to Hotel
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}