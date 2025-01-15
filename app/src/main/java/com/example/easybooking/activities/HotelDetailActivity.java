package com.example.easybooking.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.easybooking.R;
import com.example.easybooking.models.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HotelDetailActivity extends AppCompatActivity {
    private EditText checkInEditText, checkOutEditText;
    private Date checkInDate, checkOutDate;
    private TextView hotelDetailTotalPrice, hotelDetailTotalCalculation;
    private double hotelPricePerNight;
    private double hotelPricePerHour;
    private Button addToBookingButton;
    private String hotelId;
    private String hotelName;
    private String hotelLocation;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_detail);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        hotelId = getIntent().getStringExtra("HOTEL_ID");
        hotelPricePerNight = getIntent().getDoubleExtra("HOTEL_PRICE_PER_NIGHT", 0.0);
        hotelPricePerHour = getIntent().getDoubleExtra("HOTEL_PRICE_PER_HOUR", 0.0);

        // Viewable hotel data in layout
        String hotelImageUrl = getIntent().getStringExtra("HOTEL_IMAGE_URL");
        hotelName = getIntent().getStringExtra("HOTEL_NAME");
        String hotelDescription = getIntent().getStringExtra("HOTEL_DESCRIPTION");
        hotelLocation = getIntent().getStringExtra("HOTEL_LOCATION");
        double latitude = getIntent().getDoubleExtra("HOTEL_LAT", 0.0);
        double longitude = getIntent().getDoubleExtra("HOTEL_LNG", 0.0);

        ImageView hotelDetailImageView = findViewById(R.id.hotelDetailImageView);
        TextView hotelDetailNameTextView = findViewById(R.id.hotelDetailNameTextView);
        TextView hotelDetailDescriptionTextView = findViewById(R.id.hotelDetailDescriptionTextView);
        TextView hotelDetailLocationTextView = findViewById(R.id.hotelDetailLocationTextView);
        Button viewOnMapButton = findViewById(R.id.viewOnMapButton);

        hotelDetailTotalPrice = findViewById(R.id.hotelDetailTotalPrice);
        hotelDetailTotalCalculation = findViewById(R.id.hotelDetailTotalCalculation);
        checkInEditText = findViewById(R.id.hotelDetailCheckInEditText);
        checkOutEditText = findViewById(R.id.hotelDetailCheckOutEditText);

        // Set up date pickers for check-in and check-out
        setupDatePicker(checkInEditText, true);
        setupDatePicker(checkOutEditText, false);

        // View all hotel information
        Glide.with(this)
                .load(hotelImageUrl)
                .placeholder(R.drawable.ic_hotel)
                .error(R.drawable.ic_hotel)
                .into(hotelDetailImageView);
        hotelDetailNameTextView.setText(hotelName);
        hotelDetailDescriptionTextView.setText(hotelDescription);
        hotelDetailLocationTextView.setText(hotelLocation);

        // View location on map
        viewOnMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(HotelDetailActivity.this, MapActivity.class);
            intent.putExtra("NAME", hotelName);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LNG", longitude);
            startActivity(intent);
        });

        addToBookingButton = findViewById(R.id.addToBookingButton);
        addToBookingButton.setOnClickListener(v -> addToBooking());
    }

    private void setupDatePicker(EditText dateEditText, boolean isCheckIn) {
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(v -> showDatePickerDialog(dateEditText, isCheckIn));
        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showDatePickerDialog(dateEditText, isCheckIn);
        });
    }

    private void showDatePickerDialog(EditText dateEditText, boolean isCheckIn) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    new TimePickerDialog(
                            this,
                            (timePicker, hourOfDay, minute) -> {
                                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDate.set(Calendar.MINUTE, minute);
                                Date date = selectedDate.getTime();

                                if (isCheckIn) {
                                    checkInDate = date;
                                    dateEditText.setText(date.toString());
                                    dateEditText.setError(null);

                                    // Validate the check-out date if already selected
                                    if (checkOutDate != null && !checkOutDate.after(checkInDate)) {
                                        checkOutDate = null;
                                        checkOutEditText.setText("");
                                        checkOutEditText.setError("Check-out date must be after check-in date");
                                    }
                                } else {
                                    // Validate the check-out date
                                    if (checkInDate != null && !date.after(checkInDate)) {
                                        checkOutDate = null;
                                        checkOutEditText.setText("");
                                        dateEditText.setError("Check-out date must be after check-in date");
                                    } else {
                                        checkOutDate = date;
                                        dateEditText.setText(date.toString());
                                        dateEditText.setError(null);
                                    }
                                }

                                // Update total price if both dates are selected
                                updateTotalPrice();
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateTotalPrice() {
        if (checkInDate != null && checkOutDate != null) {
            // Calculate the duration in milliseconds
            long durationInMillis = checkOutDate.getTime() - checkInDate.getTime();

            // If the duration is less than 24 hours, calculate price per hour
            if (durationInMillis < 24 * 60 * 60 * 1000) {
                long durationInHours = durationInMillis / (60 * 60 * 1000); // in hours
                double totalPrice = durationInHours * hotelPricePerHour;

                // Show the total price
                hotelDetailTotalPrice.setText(String.format("Total: $%.2f", totalPrice));

                // Show the detailed calculation (hours)
                hotelDetailTotalCalculation.setText(String.format("Price: $%.2f per hour for %d hours", hotelPricePerHour, durationInHours));
            } else {
                // If the duration is 24 hours or more, calculate price per night
                long durationInNights = durationInMillis / (24 * 60 * 60 * 1000); // in nights
                double totalPrice = durationInNights * hotelPricePerNight;

                // Show the total price
                hotelDetailTotalPrice.setText(String.format("Total: $%.2f", totalPrice));

                // Show the detailed calculation (nights)
                hotelDetailTotalCalculation.setText(String.format("Price: $%.2f per night for %d nights", hotelPricePerNight, durationInNights));
            }

            hotelDetailTotalPrice.setVisibility(TextView.VISIBLE);
            hotelDetailTotalCalculation.setVisibility(TextView.VISIBLE);
        } else {
            hotelDetailTotalPrice.setVisibility(TextView.INVISIBLE);
            hotelDetailTotalCalculation.setVisibility(TextView.INVISIBLE);
        }
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
                    boolean hotelAlreadyAdded = false;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        bookingExists = true;

                        if (doc.contains("hotelId")) {
                            String existingHotelId = doc.getString("hotelId");
                            if (existingHotelId != null) {
                                hotelAlreadyAdded = true;
                                break;
                            }
                        } else {
                            // Update the existing booking with the new hotel details
                            updateBookingWithHotel(doc.getId());
                            return;
                        }
                    }

                    if (!bookingExists) {
                        createNewBooking();
                    } else if (hotelAlreadyAdded) {
                        Toast.makeText(this, "You need to cancel the current booking or remove the hotel to add a new one.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking booking status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createNewBooking() {
        if (checkInDate == null || checkOutDate == null) {
            Toast.makeText(this, "Please select valid check-in and check-out dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure total price is updated
        updateTotalPrice();

        if (hotelDetailTotalPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Failed to calculate total price. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(hotelDetailTotalPrice.getText().toString().replace("Total: $", ""));
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        Map<String, Object> booking = new HashMap<>();
        booking.put("isCurrent", true);
        booking.put("status", "pending");
        booking.put("hotelId", hotelId);
        booking.put("hotelName", hotelName);
        booking.put("hotelLocation", hotelLocation);
        booking.put("hotelFromDate", checkInDate);
        booking.put("hotelToDate", checkOutDate);
        booking.put("hotelTotal", totalPrice);
        booking.put("totalAmount", totalPrice);
        booking.put("userId", userId);

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Hotel added to booking successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add hotel to booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateBookingWithHotel(String bookingId) {
        if (checkInDate == null || checkOutDate == null) {
            Toast.makeText(this, "Please select valid check-in and check-out dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure total price is updated
        updateTotalPrice();

        if (hotelDetailTotalPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Failed to calculate total price. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(hotelDetailTotalPrice.getText().toString().replace("Total: $", ""));

        db.collection("bookings").document(bookingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double currentTotal = documentSnapshot.contains("totalAmount") ?
                                documentSnapshot.getDouble("totalAmount") : 0.0;
                        double updatedTotal = currentTotal + totalPrice;

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("hotelId", hotelId);
                        updates.put("hotelName", hotelName);
                        updates.put("hotelLocation", hotelLocation);
                        updates.put("hotelFromDate", checkInDate);
                        updates.put("hotelToDate", checkOutDate);
                        updates.put("hotelTotal", totalPrice);
                        updates.put("totalAmount", updatedTotal);

                        db.collection("bookings").document(bookingId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Hotel added to your current booking!", Toast.LENGTH_SHORT).show();
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
