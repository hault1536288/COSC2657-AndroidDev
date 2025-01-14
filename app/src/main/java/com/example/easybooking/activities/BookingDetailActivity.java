package com.example.easybooking.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easybooking.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView headerTextView, bookingIdTextView, bookingStatusTextView;
    private LinearLayout hotelDetailsLayout, transportDetailsLayout, carDetailsLayout;
    private TextView viewLocationTextView, hotelNameTextView, hotelLocationTextView, hotelStayDurationTextView, hotelTotalTextView;
    private TextView transportTypeTextView, transportBrandTextView, transportTotalTextView;
    private TextView departureLocationTextView, departureTimeTextView, departureDateTextView, arrivalLocationTextView, arrivalTimeTextView, arrivalDateTextView;
    private TextView carNameTextView, carBrandTextView, carModelTextView, carRentDurationTextView, carTotalTextView;
    private TextView totalAmountTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_detail);

        String bookingId = getIntent().getStringExtra("BOOKING_ID");
        String bookingStatus = getIntent().getStringExtra("BOOKING_STATUS");
        String hotelName = getIntent().getStringExtra("HOTEL_NAME");
        String hotelLocation = getIntent().getStringExtra("HOTEL_LOCATION");
        String hotelStayDuration = getIntent().getStringExtra("HOTEL_STAY_DURATION");
        double hotelTotal = getIntent().getDoubleExtra("HOTEL_TOTAL", 0.0);
        String transportType = getIntent().getStringExtra("TRANSPORT_TYPE");
        String transportBrand = getIntent().getStringExtra("TRANSPORT_BRAND");
        double transportTotal = getIntent().getDoubleExtra("TRANSPORT_TOTAL", 0.0);
        String departureLocation = getIntent().getStringExtra("DEPARTURE_LOCATION");
        String departureTime = getIntent().getStringExtra("DEPARTURE_TIME");
        String departureDate = getIntent().getStringExtra("DEPARTURE_DATE");
        String arrivalLocation = getIntent().getStringExtra("ARRIVAL_LOCATION");
        String arrivalTime = getIntent().getStringExtra("ARRIVAL_TIME");
        String arrivalDate = getIntent().getStringExtra("ARRIVAL_DATE");
        String carName = getIntent().getStringExtra("CAR_NAME");
        String carBrand = getIntent().getStringExtra("CAR_BRAND");
        String carModel = getIntent().getStringExtra("CAR_MODEL");
        String carRentDuration = getIntent().getStringExtra("CAR_RENT_DURATION");
        double carTotal = getIntent().getDoubleExtra("CAR_TOTAL", 0.0);
        double totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);

        // Header and Booking Info
        headerTextView = findViewById(R.id.headerTextView);
        bookingIdTextView = findViewById(R.id.bookingIdTextView);
        bookingStatusTextView = findViewById(R.id.bookingStatusTextView);

        // Layouts for different booking sections
        hotelDetailsLayout = findViewById(R.id.hotelDetailsLayout);
        transportDetailsLayout = findViewById(R.id.transportDetailsLayout);
        carDetailsLayout = findViewById(R.id.carDetailsLayout);

        // Hotel Details
        hotelNameTextView = findViewById(R.id.hotelNameTextView);
        hotelLocationTextView = findViewById(R.id.hotelLocationTextView);
        hotelStayDurationTextView = findViewById(R.id.hotelStayDurationTextView);
        hotelTotalTextView = findViewById(R.id.hotelTotalTextView);

        // Transport Details
        transportTypeTextView = findViewById(R.id.transportTypeTextView);
        transportBrandTextView = findViewById(R.id.transportBrandTextView);
        transportTotalTextView = findViewById(R.id.transportTotalTextView);

        // Departure and Arrival Info for Transport
        departureLocationTextView = findViewById(R.id.departureLocationTextView);
        departureTimeTextView = findViewById(R.id.departureTimeTextView);
        departureDateTextView = findViewById(R.id.departureDateTextView);
        arrivalLocationTextView = findViewById(R.id.arrivalLocationTextView);
        arrivalTimeTextView = findViewById(R.id.arrivalTimeTextView);
        arrivalDateTextView = findViewById(R.id.arrivalDateTextView);

        // Car Details
        carNameTextView = findViewById(R.id.carNameTextView);
        carBrandTextView = findViewById(R.id.carBrandTextView);
        carModelTextView = findViewById(R.id.carModelTextView);
        carRentDurationTextView = findViewById(R.id.carRentDurationTextView);
        carTotalTextView = findViewById(R.id.carTotalTextView);

        // Checkout Button
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        if ("pending".equalsIgnoreCase(bookingStatus)) {
            checkoutButton.setVisibility(View.VISIBLE);
        } else {
            checkoutButton.setVisibility(View.GONE);
        }

        checkoutButton.setOnClickListener(v -> showCheckoutConfirmationDialog(totalAmount, bookingId));

        // Change text color based on booking status
        if ("success".equalsIgnoreCase(bookingStatus)) {
            bookingStatusTextView.setTextColor(getResources().getColor(R.color.green));
        } else if ("cancelled".equalsIgnoreCase(bookingStatus)) {
            bookingStatusTextView.setTextColor(getResources().getColor(R.color.red));
        } else if ("pending".equalsIgnoreCase(bookingStatus)) {
            bookingStatusTextView.setTextColor(getResources().getColor(R.color.gray));
        }

        bookingIdTextView.setText(bookingId);
        bookingStatusTextView.setText(bookingStatus);

        hotelNameTextView.setText("Name: " + hotelName);
        hotelLocationTextView.setText("Location: " + hotelLocation);
        hotelStayDurationTextView.setText("Stay from: " + hotelStayDuration);
        hotelTotalTextView.setText("Total: " + String.format("%.2f", hotelTotal));

        transportTypeTextView.setText("Type: " + transportType);
        transportBrandTextView.setText("Brand: " + transportBrand);
        transportTotalTextView.setText("Total: " + String.format("%.2f", transportTotal));
        departureLocationTextView.setText(departureLocation);
        departureTimeTextView.setText(departureTime);
        departureDateTextView.setText(departureDate);
        arrivalLocationTextView.setText(arrivalLocation);
        arrivalTimeTextView.setText(arrivalTime);
        arrivalDateTextView.setText(arrivalDate);

        carNameTextView.setText("Name: " + carName);
        carBrandTextView.setText("Brand: " + carBrand);
        carModelTextView.setText("Model: " + carModel);
        carRentDurationTextView.setText("From: " + carRentDuration);
        carTotalTextView.setText(String.format("Total: " + "%.2f", carTotal));

        totalAmountTextView.setText("Total: " + String.format("%.2f", totalAmount));

        // Hide layouts if no data is available
        if (hotelName == null || hotelName.isEmpty()) {
            hotelDetailsLayout.setVisibility(View.GONE);
        }

        if (transportType == null || transportType.isEmpty()) {
            transportDetailsLayout.setVisibility(View.GONE);
        }

        if (carName == null || carName.isEmpty()) {
            carDetailsLayout.setVisibility(View.GONE);
        }
    }

    private void showCheckoutConfirmationDialog(double totalAmount, String bookingId) {
        // Create and show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment");
        builder.setMessage("Are you sure you want to finish payment with the amount: $" +
                String.format("%.2f", totalAmount) +
                "? Please ensure all booking details are correct.");

        builder.setNegativeButton("Back", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            // Update booking status to "success" in Firebase
            updateBookingStatus(bookingId, "success");
        });

        // Show the dialog
        builder.create().show();
    }

    private void updateBookingStatus(String bookingId, String status) {
        if (bookingId == null || bookingId.isEmpty()) {
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("bookings").document(bookingId)
                .update(
                        "status", status,
                        "isCurrent", false
                )
                .addOnSuccessListener(aVoid -> {
                    // Booking status updated successfully
                    showToast("Payment completed successfully!");
                    finish(); // Close the current activity
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    showToast("Failed to update booking status. Please try again.");
                });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}