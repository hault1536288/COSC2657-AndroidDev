package com.example.easybooking.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.easybooking.R;
import com.example.easybooking.activities.HotelDetailActivity;
import com.example.easybooking.adapters.HotelRecyclerViewAdapter;
import com.example.easybooking.adapters.RecyclerViewInterface;
import com.example.easybooking.models.Hotel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HotelFragment extends Fragment implements RecyclerViewInterface {

    private final ArrayList<Hotel> hotelArrayList = new ArrayList<>();
    private HotelRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    public HotelFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        ImageButton toggleSearchButton = view.findViewById(R.id.toggleSearchButton);
        View searchPanelContainer = view.findViewById(R.id.searchPanelContainer);
        
        // Set initial state
        searchPanelContainer.setVisibility(View.VISIBLE);
        toggleSearchButton.setRotation(180); // Point up initially

        // Set up toggle button click listener
        toggleSearchButton.setOnClickListener(v -> {
            boolean isVisible = searchPanelContainer.getVisibility() == View.VISIBLE;
            
            // Create animations
            Animation rotate = new RotateAnimation(
                isVisible ? 180 : 0, // From degree
                isVisible ? 0 : 180, // To degree
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            
            // Animate the arrow
            toggleSearchButton.startAnimation(rotate);

            // Animate the container
            if (isVisible) {
                searchPanelContainer.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction(() -> searchPanelContainer.setVisibility(View.GONE))
                    .start();
            } else {
                searchPanelContainer.setAlpha(0f);
                searchPanelContainer.setVisibility(View.VISIBLE);
                searchPanelContainer.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start();
            }
        });

        EditText locationEditText = view.findViewById(R.id.locationEditText);
        EditText hotelNameEditText = view.findViewById(R.id.hotelNameEditText);
        EditText minPricePerNightEditText = view.findViewById(R.id.minPricePerNightEditText);
        EditText maxPricePerNightEditText = view.findViewById(R.id.maxPricePerNightEditText);
        EditText minPricePerHourEditText = view.findViewById(R.id.minPricePerHourEditText);
        EditText maxPricePerHourEditText = view.findViewById(R.id.maxPricePerHourEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        locationEditText.clearFocus();
        hotelNameEditText.clearFocus();
        minPricePerNightEditText.clearFocus();
        maxPricePerNightEditText.clearFocus();
        minPricePerHourEditText.clearFocus();
        maxPricePerHourEditText.clearFocus();

        view.findViewById(R.id.hotelsRecyclerView).requestFocus();

        // Convert RecyclerView to local variable
        RecyclerView hotelsRecyclerView = view.findViewById(R.id.hotelsRecyclerView);
        adapter = new HotelRecyclerViewAdapter(getContext(), hotelArrayList, this);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelsRecyclerView.setAdapter(adapter);

        // Initialize FireStore
        firestore = FirebaseFirestore.getInstance();

        // Set up search button click listener
        searchButton.setOnClickListener(v -> performSearch(
            locationEditText,
            hotelNameEditText,
            minPricePerNightEditText,
            maxPricePerNightEditText,
            minPricePerHourEditText,
            maxPricePerHourEditText
        ));
        
        // Initial data load
        fetchHotelData();
    }

    private void fetchHotelData() {
        firestore.collection("hotels")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int oldSize = hotelArrayList.size();
                            hotelArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Hotel hotel = document.toObject(Hotel.class);
                                if (hotel != null) {
                                    hotel.setHotelId(document.getId());
                                    hotelArrayList.add(hotel);
                                }
                            }
                            adapter.notifyItemRangeInserted(0, hotelArrayList.size());
                        }
                    } else {
                        Log.e("HotelFragment", "Error fetching data", task.getException());
                        Toast.makeText(getContext(), "Failed to load hotels", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HotelFragment", "Error fetching data", e);
                    Toast.makeText(getContext(), "Failed to load hotels", Toast.LENGTH_SHORT).show();
                });
    }

    private void performSearch(
        EditText locationEditText,
        EditText hotelNameEditText,
        EditText minPricePerNightEditText,
        EditText maxPricePerNightEditText,
        EditText minPricePerHourEditText,
        EditText maxPricePerHourEditText
    ) {
        final String location = locationEditText.getText().toString().trim().toLowerCase();
        final String hotelName = hotelNameEditText.getText().toString().trim().toLowerCase();

        if (location.isEmpty()) {
            locationEditText.setError("Location is required");
            return;
        }

        // Parse price ranges first to validate
        final Double minPriceNight, maxPriceNight;
        final Double minPriceHour, maxPriceHour;

        try {
            String minNightStr = minPricePerNightEditText.getText().toString().trim();
            String maxNightStr = maxPricePerNightEditText.getText().toString().trim();
            String minHourStr = minPricePerHourEditText.getText().toString().trim();
            String maxHourStr = maxPricePerHourEditText.getText().toString().trim();

            minPriceNight = !minNightStr.isEmpty() ? Double.parseDouble(minNightStr) : null;
            maxPriceNight = !maxNightStr.isEmpty() ? Double.parseDouble(maxNightStr) : null;
            minPriceHour = !minHourStr.isEmpty() ? Double.parseDouble(minHourStr) : null;
            maxPriceHour = !maxHourStr.isEmpty() ? Double.parseDouble(maxHourStr) : null;

            // Validate price ranges
            if (minPriceNight != null && maxPriceNight != null && minPriceNight > maxPriceNight) {
                Toast.makeText(getContext(), "Invalid price per night range", Toast.LENGTH_SHORT).show();
                return;
            }
            if (minPriceHour != null && maxPriceHour != null && minPriceHour > maxPriceHour) {
                Toast.makeText(getContext(), "Invalid price per hour range", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get all hotels and filter in memory
        firestore.collection("hotels")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<Hotel> filteredList = new ArrayList<>();

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Hotel hotel = document.toObject(Hotel.class);
                                if (hotel != null) {
                                    hotel.setHotelId(document.getId());
                                    
                                    // Convert hotel data to lowercase for case-insensitive comparison
                                    String hotelLocation = hotel.getLocation().toLowerCase();
                                    String hotelNameLower = hotel.getHotelName().toLowerCase();
                                    
                                    // Check location (required)
                                    if (!hotelLocation.contains(location)) {
                                        continue;
                                    }

                                    // Check hotel name if provided
                                    if (!hotelName.isEmpty() && !hotelNameLower.contains(hotelName)) {
                                        continue;
                                    }

                                    // Get hotel prices
                                    double hotelPricePerNight = hotel.getPricePerNight();
                                    double hotelPricePerHour = hotel.getPricePerHour();

                                    // Check price per night range
                                    boolean nightPriceInRange = minPriceNight == null || !(hotelPricePerNight < minPriceNight);
                                    if (maxPriceNight != null && hotelPricePerNight > maxPriceNight) {
                                        nightPriceInRange = false;
                                    }

                                    // Check price per hour range
                                    boolean hourPriceInRange = minPriceHour == null || !(hotelPricePerHour < minPriceHour);
                                    if (maxPriceHour != null && hotelPricePerHour > maxPriceHour) {
                                        hourPriceInRange = false;
                                    }

                                    // Add hotel if it matches the price ranges (when specified)
                                    boolean priceRangeSpecified = minPriceNight != null || maxPriceNight != null 
                                                                || minPriceHour != null || maxPriceHour != null;
                                    
                                    if (!priceRangeSpecified || (nightPriceInRange && hourPriceInRange)) {
                                        filteredList.add(hotel);
                                    }
                                }
                            }

                            // Update the RecyclerView with filtered results
                            int oldSize = hotelArrayList.size();
                            hotelArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);
                            
                            hotelArrayList.addAll(filteredList);
                            adapter.notifyItemRangeInserted(0, hotelArrayList.size());

                            if (hotelArrayList.isEmpty()) {
                                Toast.makeText(getContext(), "No hotels found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
                        Log.e("HotelFragment", "Error searching hotels", task.getException());
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), HotelDetailActivity.class);
        // Get the selected hotel
        Hotel selectedHotel = hotelArrayList.get(position);
        // Pass the selected hotel to the HotelDetailActivity
        intent.putExtra("HOTEL_ID", selectedHotel.getHotelId());
        intent.putExtra("HOTEL_PRICE_PER_NIGHT", selectedHotel.getPricePerNight());
        intent.putExtra("HOTEL_PRICE_PER_HOUR", selectedHotel.getPricePerHour());

        // Viewable hotel data in layout
        intent.putExtra("HOTEL_IMAGE_URL", selectedHotel.getImageUrl());
        intent.putExtra("HOTEL_NAME", selectedHotel.getHotelName());
        intent.putExtra("HOTEL_DESCRIPTION", selectedHotel.getDescription());
        intent.putExtra("HOTEL_LOCATION", selectedHotel.getLocation());
        intent.putExtra("HOTEL_LAT", selectedHotel.getLatitude());
        intent.putExtra("HOTEL_LNG", selectedHotel.getLongitude());

        startActivity(intent);
    }
}