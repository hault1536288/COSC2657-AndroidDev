package com.example.easybooking.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easybooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransportTicketDetailActivity extends AppCompatActivity {
    private Date departureDateTime, arrivalDateTime;
    private String departureLocation, arrivalLocation;
    private String transportId, transportType, transportBrand;
    private double price;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transport_ticket_detail);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        departureDateTime = (Date) getIntent().getSerializableExtra("DEPARTURE_DATETIME");
        arrivalDateTime = (Date) getIntent().getSerializableExtra("ARRIVAL_DATETIME");

        transportId = getIntent().getStringExtra("TRANSPORT_ID");
        transportType = getIntent().getStringExtra("TRANSPORT_TYPE");
        transportBrand = getIntent().getStringExtra("BRAND");
        departureLocation = getIntent().getStringExtra("DEPARTURE_LOCATION");
        String departureTime = getIntent().getStringExtra("DEPARTURE_TIME");
        String departureDate = getIntent().getStringExtra("DEPARTURE_DATE");
        arrivalLocation = getIntent().getStringExtra("ARRIVAL_LOCATION");
        String arrivalTime = getIntent().getStringExtra("ARRIVAL_TIME");
        String arrivalDate = getIntent().getStringExtra("ARRIVAL_DATE");
        price = getIntent().getDoubleExtra("PRICE", 0.0);

        // Set the ticket details in the layout
        TextView transportIdTextView = findViewById(R.id.transportIdTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView brandTextView = findViewById(R.id.brandTextView);
        TextView departureLocationTextView = findViewById(R.id.departureLocationTextView);
        TextView departureTimeTextView = findViewById(R.id.departureTimeTextView);
        TextView departureDateTextView = findViewById(R.id.departureDateTextView);
        TextView arrivalLocationTextView = findViewById(R.id.arrivalLocationTextView);
        TextView arrivalTimeTextView = findViewById(R.id.arrivalTimeTextView);
        TextView arrivalDateTextView = findViewById(R.id.arrivalDateTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);

        transportIdTextView.setText(transportId);
        typeTextView.setText(transportType);
        brandTextView.setText(transportBrand);
        departureLocationTextView.setText(departureLocation);
        departureTimeTextView.setText(departureTime);
        departureDateTextView.setText(departureDate);
        arrivalLocationTextView.setText(arrivalLocation);
        arrivalTimeTextView.setText(arrivalTime);
        arrivalDateTextView.setText(arrivalDate);
        priceTextView.setText(String.valueOf(price));

        Button bookNowButton = findViewById(R.id.bookNowButton);
        bookNowButton.setOnClickListener(v -> {
            showConfirmationDialog();
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Booking")
                .setMessage("Are you sure you want to book this ticket?")
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog, id) -> {
                    addToBooking();
                })
                .setNegativeButton("Back", (dialog, id) -> {
                    dialog.dismiss();
                });

        // Show the dialog
        builder.create().show();
    }

    private void addToBooking() {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("bookings")
                .whereEqualTo("isCurrent", true)
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean bookingExists = false;
                    boolean transportAlreadyAdded = false;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        bookingExists = true;

                        if (doc.contains("transportId")) {
                            String existingTransportId = doc.getString("transportId");
                            if (existingTransportId != null) {
                                transportAlreadyAdded = true;
                                break;
                            }
                        } else {
                            updateBookingWithTransport(doc.getId());
                            return;
                        }
                    }

                    if (!bookingExists) {
                        createNewBooking();
                    } else if (transportAlreadyAdded) {
                        Toast.makeText(this, "You need to cancel the current booking or remove the transport to add a new one.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking booking status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createNewBooking() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        Map<String, Object> booking = new HashMap<>();
        booking.put("isCurrent", true);
        booking.put("status", "pending");
        booking.put("transportId", transportId);
        booking.put("transportBrand", transportBrand);
        booking.put("transportType", transportType);
        booking.put("transportDepartureLocation", departureLocation);
        booking.put("transportArrivalLocation", arrivalLocation);
        booking.put("transportDepartureDate", departureDateTime);
        booking.put("transportArrivalDate", arrivalDateTime);
        booking.put("transportTotal", price);
        booking.put("totalAmount", price);
        booking.put("userId", userId);

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Transport added to booking successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add car to booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateBookingWithTransport(String bookingId) {
        db.collection("bookings").document(bookingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double currentTotal = documentSnapshot.contains("totalAmount") ?
                                documentSnapshot.getDouble("totalAmount") : 0.0;
                        double updatedTotal = currentTotal + price;

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("transportId", transportId);
                        updates.put("transportBrand", transportBrand);
                        updates.put("transportType", transportType);
                        updates.put("transportDepartureLocation", departureLocation);
                        updates.put("transportArrivalLocation", arrivalLocation);
                        updates.put("transportDepartureDate", departureDateTime);
                        updates.put("transportArrivalDate", arrivalDateTime);
                        updates.put("transportTotal", price);
                        updates.put("totalAmount", updatedTotal);

                        db.collection("bookings").document(bookingId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Transport added to your current booking!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error retrieving booking details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}