package com.example.easybooking.fragments;

import android.app.DatePickerDialog;
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
import com.example.easybooking.activities.TransportTicketDetailActivity;
import com.example.easybooking.adapters.RecyclerViewInterface;
import com.example.easybooking.adapters.TransportRecyclerViewAdapter;
import com.example.easybooking.models.Transport;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransportFragment extends Fragment implements RecyclerViewInterface {

    private final ArrayList<Transport> transportArrayList = new ArrayList<>();
    private TransportRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    public TransportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        ImageButton toggleSearchButton = view.findViewById(R.id.toggleSearchButton);
        View searchPanelContainer = view.findViewById(R.id.searchPanelContainer);
        
        // Set initial state
        searchPanelContainer.setVisibility(View.VISIBLE);
        toggleSearchButton.setRotation(0); // Point up initially

        // Set up toggle button click listener
        toggleSearchButton.setOnClickListener(v -> {
            boolean isVisible = searchPanelContainer.getVisibility() == View.VISIBLE;
            
            // Create animations
            Animation rotate = new RotateAnimation(
                isVisible ? 0 : 180,
                isVisible ? 180 : 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            
            toggleSearchButton.startAnimation(rotate);

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
        RecyclerView transportsRecyclerView = view.findViewById(R.id.transportsRecyclerView);
        adapter = new TransportRecyclerViewAdapter(getContext(), transportArrayList, this);
        transportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transportsRecyclerView.setAdapter(adapter);

        // Initialize FireStore
        firestore = FirebaseFirestore.getInstance();

        // Initialize search views
        EditText typeEditText = view.findViewById(R.id.typeEditText);
        EditText brandEditText = view.findViewById(R.id.brandEditText);
        EditText departureLocationEditText = view.findViewById(R.id.departureLocationEditText);
        EditText arrivalLocationEditText = view.findViewById(R.id.arrivalLocationEditText);
        EditText departureDateEditText = view.findViewById(R.id.departureDateEditText);
        EditText arrivalDateEditText = view.findViewById(R.id.arrivalDateEditText);
        EditText minPriceEditText = view.findViewById(R.id.minPriceEditText);
        EditText maxPriceEditText = view.findViewById(R.id.maxPriceEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

        // Set up date pickers
        setupDatePicker(departureDateEditText, "Select Departure Date");
        setupDatePicker(arrivalDateEditText, "Select Arrival Date");

        // Set up search button click listener
        searchButton.setOnClickListener(v -> performSearch(
            typeEditText,
            brandEditText,
            departureLocationEditText,
            arrivalLocationEditText,
            departureDateEditText,
            arrivalDateEditText,
            minPriceEditText,
            maxPriceEditText
        ));

        // Initial data load
        fetchTransportData();
    }

    private void setupDatePicker(EditText dateEditText, String title) {
        dateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dateEditText.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.setTitle(title);
            datePickerDialog.show();
        });
    }

    private void performSearch(
        final EditText typeEditText,
        final EditText brandEditText,
        final EditText departureLocationEditText,
        final EditText arrivalLocationEditText,
        final EditText departureDateEditText,
        final EditText arrivalDateEditText,
        final EditText minPriceEditText,
        final EditText maxPriceEditText
    ) {
        // Get search terms
        String type = typeEditText.getText().toString().trim().toLowerCase();
        String brand = brandEditText.getText().toString().trim().toLowerCase();
        String departureLocation = departureLocationEditText.getText().toString().trim();
        String arrivalLocation = arrivalLocationEditText.getText().toString().trim();
        String departureDate = departureDateEditText.getText().toString().trim();
        String arrivalDate = arrivalDateEditText.getText().toString().trim();

        // Validate required fields
        if (departureLocation.isEmpty() || arrivalLocation.isEmpty() || 
            departureDate.isEmpty() || arrivalDate.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse price range
        Double minPrice = null;
        Double maxPrice = null;
        try {
            String minPriceStr = minPriceEditText.getText().toString().trim();
            String maxPriceStr = maxPriceEditText.getText().toString().trim();

            if (!minPriceStr.isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr);
            }
            if (!maxPriceStr.isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr);
            }

            // Validate price range
            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                Toast.makeText(getContext(), "Invalid price range", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse dates
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date departureDateObj;
        Date arrivalDateObj;
        try {
            departureDateObj = dateFormat.parse(departureDate);
            arrivalDateObj = dateFormat.parse(arrivalDate);

            if (departureDateObj == null || arrivalDateObj == null) {
                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (departureDateObj.after(arrivalDateObj)) {
                Toast.makeText(getContext(), "Departure date must be before arrival date", 
                             Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store final date objects for the query
        final Date finalDepartureDate = departureDateObj;
        final Date finalArrivalDate = arrivalDateObj;
        final Double finalMinPrice = minPrice;
        final Double finalMaxPrice = maxPrice;

        // Fetch and filter transports
        firestore.collection("transports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<Transport> filteredList = new ArrayList<>();

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Transport transport = document.toObject(Transport.class);
                                if (transport != null) {
                                    transport.setTransportId(document.getId());
                                    
                                    // Check optional fields first
                                    if (!type.isEmpty() && !transport.getType().toLowerCase().contains(type)) continue;
                                    if (!brand.isEmpty() && !transport.getBrand().toLowerCase().contains(brand)) continue;
                                    
                                    // Check required fields
                                    if (!transport.getDepartureLocation().toLowerCase().contains(departureLocation.toLowerCase())) continue;
                                    if (!transport.getArrivalLocation().toLowerCase().contains(arrivalLocation.toLowerCase())) continue;
                                    
                                    // Check dates
                                    Date transportDepartureDate = transport.getDepartureDate();
                                    Date transportArrivalDate = transport.getArrivalDate();
                                    
                                    if (isSameDay(transportDepartureDate, finalDepartureDate)) continue;
                                    if (isSameDay(transportArrivalDate, finalArrivalDate)) continue;

                                    // Check price range if specified
                                    double transportPrice = transport.getPrice();
                                    if (finalMinPrice != null && transportPrice < finalMinPrice) continue;
                                    if (finalMaxPrice != null && transportPrice > finalMaxPrice) continue;

                                    filteredList.add(transport);
                                }
                            }

                            // Update the RecyclerView with filtered results
                            int oldSize = transportArrayList.size();
                            transportArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);
                            
                            transportArrayList.addAll(filteredList);
                            adapter.notifyItemRangeInserted(0, transportArrayList.size());

                            if (transportArrayList.isEmpty()) {
                                Toast.makeText(getContext(), "No transports found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
                        Log.e("TransportFragment", "Error searching transports", task.getException());
                    }
                });
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) return true;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) ||
                cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void fetchTransportData() {
        firestore.collection("transports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Clear existing data
                            int oldSize = transportArrayList.size();
                            transportArrayList.clear();
                            adapter.notifyItemRangeRemoved(0, oldSize);

                            // Add new data
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Transport transport = document.toObject(Transport.class);
                                if (transport != null) {
                                    transport.setTransportId(document.getId());
                                    transportArrayList.add(transport);
                                }
                            }
                            
                            // Notify about new items
                            adapter.notifyItemRangeInserted(0, transportArrayList.size());
                        }
                    } else {
                        Log.e("TransportFragment", "Error fetching transports", task.getException());
                        Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), TransportTicketDetailActivity.class);
        Transport selectedTransport = transportArrayList.get(position);

        // Create SimpleDateFormat objects for time and date
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Format departure and arrival times and dates
        String departureTime = timeFormat.format(selectedTransport.getDepartureDate());
        String departureDate = dateFormat.format(selectedTransport.getDepartureDate());
        String arrivalTime = timeFormat.format(selectedTransport.getArrivalDate());
        String arrivalDate = dateFormat.format(selectedTransport.getArrivalDate());

        intent.putExtra("TRANSPORT_ID", selectedTransport.getTransportId());
        intent.putExtra("TRANSPORT_TYPE", selectedTransport.getType());
        intent.putExtra("BRAND", selectedTransport.getBrand());
        intent.putExtra("DEPARTURE_LOCATION", selectedTransport.getDepartureLocation());
        intent.putExtra("DEPARTURE_DATETIME", selectedTransport.getDepartureDate());
        intent.putExtra("DEPARTURE_TIME", departureTime);
        intent.putExtra("DEPARTURE_DATE", departureDate);
        intent.putExtra("ARRIVAL_LOCATION", selectedTransport.getArrivalLocation());
        intent.putExtra("ARRIVAL_DATETIME", selectedTransport.getArrivalDate());
        intent.putExtra("ARRIVAL_TIME", arrivalTime);
        intent.putExtra("ARRIVAL_DATE", arrivalDate);
        intent.putExtra("PRICE", selectedTransport.getPrice());

        startActivity(intent);
    }
}
