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

        // Setup Current Booking RecyclerView
        RecyclerView currentBookingRecyclerView = view.findViewById(R.id.currentBookingRecyclerView);
        currentBookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup History RecyclerView
        RecyclerView historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data for Current Booking
        List<Booking> currentBookings = new ArrayList<>();
        currentBookings.add(new Booking("#123456", "11/20/2024 → 11/23/2024", "Hotel Name 2", "Location A → Location B", "$450 USD", "Pending", true));

        // Sample data for History
        List<Booking> historyBookings = new ArrayList<>();
        historyBookings.add(new Booking("#123457", "11/25/2024 → 11/28/2024", "Hotel Name", "Location B → Location C", "$350 USD", "Success", false));
        historyBookings.add(new Booking("#123458", "10/20/2024 → 10/23/2024", "Hotel Name 3", "Location C → Location D", "$500 USD", "Success", false));

        // Set adapters
        BookingAdapter currentAdapter = new BookingAdapter(currentBookings, getContext());
        currentBookingRecyclerView.setAdapter(currentAdapter);

        BookingAdapter historyAdapter = new BookingAdapter(historyBookings, getContext());
        historyRecyclerView.setAdapter(historyAdapter);

        return view;
    }
}
