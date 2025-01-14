package com.example.easybooking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easybooking.fragments.BookingFragment;
import com.example.easybooking.fragments.CarFragment;
import com.example.easybooking.fragments.HotelFragment;
import com.example.easybooking.fragments.ProfileFragment;
import com.example.easybooking.fragments.TransportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.hotel) {
                replaceFragment(new HotelFragment());
            } else if (itemId == R.id.car) {
                replaceFragment(new CarFragment());
            } else if (itemId == R.id.transport) {
                replaceFragment(new TransportFragment());
            } else if (itemId == R.id.booking) {
                replaceFragment(new BookingFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        // Set initial fragment to BookingFragment
        replaceFragment(new BookingFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
