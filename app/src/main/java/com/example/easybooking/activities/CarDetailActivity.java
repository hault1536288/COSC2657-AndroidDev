package com.example.easybooking.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarDetailActivity extends AppCompatActivity {
    private EditText carDetailRentFromEditText, carDetailRentToEditText;
    private Date rentFromDate, rentToDate;
    private TextView carDetailTotalPrice, carDetailTotalCalculation;
    private double carPricePerDay;
    private double carPricePerHour;
    private Button addToBookingButton;
    private String carId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_detail);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        carId = getIntent().getStringExtra("CAR_ID");
        // Data that get viewed on layout
        String carImageUrl = getIntent().getStringExtra("CAR_IMAGE_URL");
        String carName = getIntent().getStringExtra("CAR_NAME");
        String carBrand = getIntent().getStringExtra("CAR_BRAND");
        String carModel = getIntent().getStringExtra("CAR_MODEL");
        String carDescription = getIntent().getStringExtra("CAR_DESCRIPTION");
        carPricePerDay = getIntent().getDoubleExtra("CAR_PRICE_PER_DAY", 0.0);
        carPricePerHour = getIntent().getDoubleExtra("CAR_PRICE_PER_HOUR", 0.0);

        // View on layout
        ImageView carDetailImageView = findViewById(R.id.carDetailImageView);
        TextView carDetailNameTextView = findViewById(R.id.carDetailNameTextView);
        TextView carDetailBrandTextView = findViewById(R.id.carDetailBrandTextView);
        TextView carDetailModelTextView = findViewById(R.id.carDetailModelTextView);
        TextView carDetailDescriptionTextView = findViewById(R.id.carDetailDescriptionTextView);
        TextView carDetailPriceTextView = findViewById(R.id.carDetailPriceTextView);

        carDetailRentFromEditText = findViewById(R.id.carDetailRentFromEditText);
        carDetailRentToEditText = findViewById(R.id.carDetailRentToEditText);
        carDetailTotalPrice = findViewById(R.id.carTotalPriceTextView);
        carDetailTotalCalculation = findViewById(R.id.carTotalCalculationTextView);

        // Set up date pickers for rent start and end dates
        setupDatePicker(carDetailRentFromEditText, true);
        setupDatePicker(carDetailRentToEditText, false);

        Glide.with(this)
                .load(carImageUrl)
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car)
                .into(carDetailImageView);
        carDetailNameTextView.setText(carName);
        carDetailBrandTextView.setText(carBrand);
        carDetailModelTextView.setText(carModel);
        carDetailDescriptionTextView.setText(carDescription);
        carDetailPriceTextView.setText("$" + carPricePerDay + "/day" + " or $" + carPricePerHour + "/hour");

        addToBookingButton = findViewById(R.id.carDetailAddToBookingButton);
        addToBookingButton.setOnClickListener(v -> addToBooking());
    }

    private void setupDatePicker(EditText dateEditText, boolean isRentFrom) {
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(v -> showDatePickerDialog(dateEditText, isRentFrom));
        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showDatePickerDialog(dateEditText, isRentFrom);
        });
    }

    private void showDatePickerDialog(EditText dateEditText, boolean isRentFrom) {
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

                                if (isRentFrom) {
                                    rentFromDate = date;
                                    dateEditText.setText(date.toString());
                                    dateEditText.setError(null);

                                    if (rentToDate != null && !rentToDate.after(rentFromDate)) {
                                        rentToDate = null;
                                        carDetailRentToEditText.setText("");
                                        carDetailRentToEditText.setError("Rent-to date must be after rent-from date");
                                    }
                                } else {
                                    if (rentFromDate != null && !date.after(rentFromDate)) {
                                        rentToDate = null;
                                        carDetailRentToEditText.setText("");
                                        dateEditText.setError("Rent-to date must be after rent-from date");
                                    } else {
                                        rentToDate = date;
                                        dateEditText.setText(date.toString());
                                        dateEditText.setError(null);
                                    }
                                }

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
        if (rentFromDate != null && rentToDate != null) {
            long durationInMillis = rentToDate.getTime() - rentFromDate.getTime();

            if (durationInMillis < 24 * 60 * 60 * 1000) {
                long durationInHours = durationInMillis / (60 * 60 * 1000); // in hours
                double totalPrice = durationInHours * carPricePerHour;
                carDetailTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
                carDetailTotalCalculation.setText(String.format("Price: $%.2f per hour for %d hours", carPricePerHour, durationInHours));
            } else {
                long durationInDays = durationInMillis / (24 * 60 * 60 * 1000); // in days
                double totalPrice = durationInDays * carPricePerDay;
                carDetailTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
                carDetailTotalCalculation.setText(String.format("Price: $%.2f per day for %d days", carPricePerDay, durationInDays));
            }

            carDetailTotalPrice.setVisibility(TextView.VISIBLE);
            carDetailTotalCalculation.setVisibility(TextView.VISIBLE);
        } else {
            carDetailTotalPrice.setVisibility(TextView.INVISIBLE);
            carDetailTotalCalculation.setVisibility(TextView.INVISIBLE);
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
                    boolean carAlreadyAdded = false;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        bookingExists = true;

                        if (doc.contains("carId")) {
                            String existingCarId = doc.getString("carId");
                            if (existingCarId != null) {
                                carAlreadyAdded = true;
                                break;
                            }
                        } else {
                            updateBookingWithCar(doc.getId());
                            return;
                        }
                    }

                    if (!bookingExists) {
                        createNewBooking();
                    } else if (carAlreadyAdded) {
                        Toast.makeText(this, "You need to cancel the current booking or remove the car to add a new one.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking booking status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createNewBooking() {
        if (rentFromDate == null || rentToDate == null) {
            Toast.makeText(this, "Please select valid rent-from and rent-to dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        updateTotalPrice();

        if (carDetailTotalPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Failed to calculate total price. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(carDetailTotalPrice.getText().toString().replace("Total: $", ""));
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        Map<String, Object> booking = new HashMap<>();
        booking.put("isCurrent", true);
        booking.put("status", "pending");
        booking.put("carId", carId);
        booking.put("carName", getIntent().getStringExtra("CAR_NAME"));
        booking.put("carBrand", getIntent().getStringExtra("CAR_BRAND"));
        booking.put("carModel", getIntent().getStringExtra("CAR_MODEL"));
        booking.put("carFromDate", rentFromDate);
        booking.put("carToDate", rentToDate);
        booking.put("carTotal", totalPrice);
        booking.put("totalAmount", totalPrice);
        booking.put("userId", userId);

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Car added to booking successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add car to booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateBookingWithCar(String bookingId) {
        if (rentFromDate == null || rentToDate == null) {
            Toast.makeText(this, "Please select valid rent-from and rent-to dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        updateTotalPrice();

        if (carDetailTotalPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Failed to calculate total price. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(carDetailTotalPrice.getText().toString().replace("Total: $", ""));

        db.collection("bookings").document(bookingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double currentTotal = documentSnapshot.contains("totalAmount") ? documentSnapshot.getDouble("totalAmount") : 0.0;
                        double updatedTotal = currentTotal + totalPrice;

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("carId", carId);
                        updates.put("carName", getIntent().getStringExtra("CAR_NAME"));
                        updates.put("carBrand", getIntent().getStringExtra("CAR_BRAND"));
                        updates.put("carModel", getIntent().getStringExtra("CAR_MODEL"));
                        updates.put("carFromDate", rentFromDate);
                        updates.put("carToDate", rentToDate);
                        updates.put("carTotal", totalPrice);
                        updates.put("totalAmount", updatedTotal);

                        db.collection("bookings").document(bookingId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Car added to your current booking!", Toast.LENGTH_SHORT).show();
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
