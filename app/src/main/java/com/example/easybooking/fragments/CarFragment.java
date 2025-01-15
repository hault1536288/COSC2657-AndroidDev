package com.example.easybooking.fragments;

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
import com.example.easybooking.adapters.CarRecyclerViewAdapter;
import com.example.easybooking.models.Car;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarFragment extends Fragment {
    private final ArrayList<Car> carArrayList = new ArrayList<>();
    private CarRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false);
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
                isVisible ? 180 : 0,
                isVisible ? 0 : 180,
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

        // Initialize RecyclerView
        RecyclerView carsRecyclerView = view.findViewById(R.id.carsRecyclerView);
        adapter = new CarRecyclerViewAdapter(getContext(), carArrayList);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carsRecyclerView.setAdapter(adapter);

        // Initialize FireStore
        firestore = FirebaseFirestore.getInstance();

        // Initialize search views
        EditText brandEditText = view.findViewById(R.id.brandEditText);
        EditText modelEditText = view.findViewById(R.id.modelEditText);
        EditText carNameEditText = view.findViewById(R.id.carNameEditText);
        EditText minPricePerDayEditText = view.findViewById(R.id.minPricePerDayEditText);
        EditText maxPricePerDayEditText = view.findViewById(R.id.maxPricePerDayEditText);
        EditText minPricePerHourEditText = view.findViewById(R.id.minPricePerHourEditText);
        EditText maxPricePerHourEditText = view.findViewById(R.id.maxPricePerHourEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        // Set up search button click listener
        searchButton.setOnClickListener(v -> performSearch(
            brandEditText,
            modelEditText,
            carNameEditText,
            minPricePerDayEditText,
            maxPricePerDayEditText,
            minPricePerHourEditText,
            maxPricePerHourEditText
        ));

        // Initial data load
        fetchCarData();
    }

    private void performSearch(
        EditText brandEditText,
        EditText modelEditText,
        EditText carNameEditText,
        EditText minPricePerDayEditText,
        EditText maxPricePerDayEditText,
        EditText minPricePerHourEditText,
        EditText maxPricePerHourEditText
    ) {
        // Get search terms and convert to lowercase for case-insensitive search
        String brand = brandEditText.getText().toString().trim().toLowerCase();
        String model = modelEditText.getText().toString().trim().toLowerCase();
        String carName = carNameEditText.getText().toString().trim().toLowerCase();

        // Parse price ranges
        final Double minPriceDay, maxPriceDay, minPriceHour, maxPriceHour;

        try {
            String minDayStr = minPricePerDayEditText.getText().toString().trim();
            String maxDayStr = maxPricePerDayEditText.getText().toString().trim();
            String minHourStr = minPricePerHourEditText.getText().toString().trim();
            String maxHourStr = maxPricePerHourEditText.getText().toString().trim();

            minPriceDay = !minDayStr.isEmpty() ? Double.parseDouble(minDayStr) : null;
            maxPriceDay = !maxDayStr.isEmpty() ? Double.parseDouble(maxDayStr) : null;
            minPriceHour = !minHourStr.isEmpty() ? Double.parseDouble(minHourStr) : null;
            maxPriceHour = !maxHourStr.isEmpty() ? Double.parseDouble(maxHourStr) : null;

            // Validate price ranges
            if ((minPriceDay != null && maxPriceDay != null && minPriceDay > maxPriceDay) ||
                (minPriceHour != null && maxPriceHour != null && minPriceHour > maxPriceHour)) {
                Toast.makeText(getContext(), "Invalid price range", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch and filter cars
        firestore.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<Car> filteredList = new ArrayList<>();

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Car car = document.toObject(Car.class);
                                if (car != null) {
                                    car.setCarId(document.getId());
                                    
                                    // Convert car data to lowercase for case-insensitive comparison
                                    String carBrand = car.getBrand().toLowerCase();
                                    String carModel = car.getModel().toLowerCase();
                                    String carNameLower = car.getName().toLowerCase();
                                    
                                    // Check if car matches search criteria
                                    if (!brand.isEmpty() && !carBrand.contains(brand)) continue;
                                    if (!model.isEmpty() && !carModel.contains(model)) continue;
                                    if (!carName.isEmpty() && !carNameLower.contains(carName)) continue;

                                    // Get car prices
                                    double carPricePerDay = car.getPricePerDay();
                                    double carPricePerHour = car.getPricePerHour();

                                    // Check price per day range
                                    boolean dayPriceInRange = minPriceDay == null || !(carPricePerDay < minPriceDay);
                                    if (maxPriceDay != null && carPricePerDay > maxPriceDay) {
                                        dayPriceInRange = false;
                                    }

                                    // Check price per hour range
                                    boolean hourPriceInRange = minPriceHour == null || !(carPricePerHour < minPriceHour);
                                    if (maxPriceHour != null && carPricePerHour > maxPriceHour) {
                                        hourPriceInRange = false;
                                    }

                                    // Add car if it matches the price ranges (when specified)
                                    boolean priceRangeSpecified = minPriceDay != null || maxPriceDay != null 
                                                                || minPriceHour != null || maxPriceHour != null;
                                    
                                    if (!priceRangeSpecified || (dayPriceInRange && hourPriceInRange)) {
                                        filteredList.add(car);
                                    }
                                }
                            }

                            // Update the RecyclerView with filtered results
                            int oldSize = carArrayList.size();
                            carArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);
                            
                            carArrayList.addAll(filteredList);
                            adapter.notifyItemRangeInserted(0, carArrayList.size());

                            if (carArrayList.isEmpty()) {
                                Toast.makeText(getContext(), "No cars found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
                        Log.e("CarFragment", "Error searching cars", task.getException());
                    }
                });
    }

    private void fetchCarData() {
        firestore.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Clear existing data
                            int oldSize = carArrayList.size();
                            carArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);

                            // Add new data
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Car car = document.toObject(Car.class);
                                if (car != null) {
                                    car.setCarId(document.getId());
                                    carArrayList.add(car);
                                }
                            }
                            
                            // Notify about new items
                            adapter.notifyItemRangeInserted(0, carArrayList.size());
                        }
                    } else {
                        Log.e("CarFragment", "Error fetching cars", task.getException());
                        Toast.makeText(getContext(), "Failed to load transport", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}