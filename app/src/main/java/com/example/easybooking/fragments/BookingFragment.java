package com.example.easybooking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.adapters.BookingAdapter;
import com.example.easybooking.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        RecyclerView currentBookingRecyclerView = view.findViewById(R.id.currentBookingRecyclerView);
        RecyclerView historyRecyclerView = view.findViewById(R.id.historyRecyclerView);

        // Setting layout managers
        currentBookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data for current bookings
        List<Booking> currentBookings = new ArrayList<>();
        currentBookings.add(new Booking("#123456", "11/20/2024 → 11/23/2024", "Hotel Name 2", "Location A → Location B", "N/A", "$450 USD", "Pending", true));

        // Sample data for history bookings
        List<Booking> historyBookings = new ArrayList<>();
        historyBookings.add(new Booking("#123457", "11/25/2024 → 11/28/2024", "Hotel Name 3", "Location C → Location D", "Car Name", "$350 USD", "Success", false));
        historyBookings.add(new Booking("#123458", "10/20/2024 → 10/23/2024", "Hotel Name 4", "Location E → Location F", null, "$500 USD", "Success", false));

        // Creating adapters for current and history bookings
        BookingAdapter currentAdapter = new BookingAdapter(currentBookings, this);
        BookingAdapter historyAdapter = new BookingAdapter(historyBookings, this);

        // Setting adapters to RecyclerViews
        currentBookingRecyclerView.setAdapter(currentAdapter);
        historyRecyclerView.setAdapter(historyAdapter);

        return view;
    }

    // Method to navigate to CheckoutFragment
    public void navigateToCheckoutFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new CheckoutFragment())
                .addToBackStack(null)
                .commit();
    }
}
