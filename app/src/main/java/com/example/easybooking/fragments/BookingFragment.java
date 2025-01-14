package com.example.easybooking.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.adapters.BookingRecyclerViewAdapter;
import com.example.easybooking.models.Booking;
import com.example.easybooking.models.Car;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class BookingFragment extends Fragment {
    private RecyclerView currentBookingRecyclerView, historyRecyclerView;
    private ArrayList<Booking> currentBookingList, historyBookingList;
    private BookingRecyclerViewAdapter currentBookingAdapter, historyBookingAdapter;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private FirebaseFirestore firestore;
    private TextView noCurrentBookingMessage, noHistoryMessage;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        // Initialize the RecyclerViews
        currentBookingRecyclerView = view.findViewById(R.id.currentBookingRecyclerView);
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        noCurrentBookingMessage = view.findViewById(R.id.noCurrentBookingMessage);
        noHistoryMessage = view.findViewById(R.id.noHistoryMessage);

        currentBookingList = new ArrayList<>();
        historyBookingList = new ArrayList<>();

        // Set up the adapters
        currentBookingAdapter = new BookingRecyclerViewAdapter(getContext(), currentBookingList);
        historyBookingAdapter = new BookingRecyclerViewAdapter(getContext(), historyBookingList);

        currentBookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentBookingRecyclerView.setAdapter(currentBookingAdapter);
        historyRecyclerView.setAdapter(historyBookingAdapter);

        // Initialize Firestore
        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        firestore = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchBookingData();

        return view;
    }

    private void fetchBookingData() {
        firestore.collection("bookings")
                .whereEqualTo("userId", currentUserId) // Filter bookings where userId matches the current user's ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Clear both lists before adding new data
                            currentBookingList.clear();
                            historyBookingList.clear();

                            // Loop through each booking document
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Booking booking = document.toObject(Booking.class);
                                if (booking != null) {
                                    booking.setBookingId(document.getId()); // Set document ID as booking ID

                                    // Check the booking status and add to the appropriate list
                                    if ("pending".equalsIgnoreCase(booking.getStatus())) {
                                        currentBookingList.add(booking);
                                    } else {
                                        historyBookingList.add(booking);
                                    }
                                }
                            }

                            // Show or hide the empty messages
                            if (currentBookingList.isEmpty()) {
                                noCurrentBookingMessage.setVisibility(View.VISIBLE);
                            } else {
                                noCurrentBookingMessage.setVisibility(View.GONE);
                            }

                            if (historyBookingList.isEmpty()) {
                                noHistoryMessage.setVisibility(View.VISIBLE);
                            } else {
                                noHistoryMessage.setVisibility(View.GONE);
                            }

                            // Notify the adapters to update the RecyclerViews
                            currentBookingAdapter.notifyDataSetChanged();
                            historyBookingAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("BookingFragment", "Error fetching data", task.getException());
                        Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("BookingFragment", "Error fetching data", e);
                    Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });
    }
}
