package com.example.easybooking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easybooking.fragments.CreateCarFragment;
import com.example.easybooking.fragments.CreateHotelFragment;
import com.example.easybooking.fragments.CreateTransportFragment;
import com.example.easybooking.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        BottomNavigationView adminBottomNavigationView = findViewById(R.id.admin_bottom_navigation);
        adminBottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.create_hotel) {
                replaceFragment(new CreateHotelFragment());
            } else if (itemId == R.id.create_car) {
                replaceFragment(new CreateCarFragment());
            } else if (itemId == R.id.create_transport) {
                replaceFragment(new CreateTransportFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
        replaceFragment(new CreateHotelFragment()); // Set initial fragment to Hotel
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
