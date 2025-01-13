package com.example.easybooking.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easybooking.R;
import com.example.easybooking.models.Transport;
import com.example.easybooking.utils.FirestoreUtils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTransportFragment extends Fragment {

    private EditText brandEditText, departureLocationEditText, arrivalLocationEditText;
    private EditText departureLatitudeEditText, departureLongitudeEditText;
    private EditText arrivalLatitudeEditText, arrivalLongitudeEditText, priceEditText;
    private EditText departureDateEditText, arrivalDateEditText;
    private Spinner transportTypeSpinner;

    private Date departureDate, arrivalDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm", Locale.getDefault());

    public CreateTransportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_transport, container, false);

        // Initialize UI components
        transportTypeSpinner = view.findViewById(R.id.transportTypeSpinner);
        brandEditText = view.findViewById(R.id.brandEditText);
        departureLocationEditText = view.findViewById(R.id.departureLocationEditText);
        arrivalLocationEditText = view.findViewById(R.id.arrivalLocationEditText);
        departureLatitudeEditText = view.findViewById(R.id.departureLatitudeEditText);
        departureLongitudeEditText = view.findViewById(R.id.departureLongitudeEditText);
        arrivalLatitudeEditText = view.findViewById(R.id.arrivalLatitudeEditText);
        arrivalLongitudeEditText = view.findViewById(R.id.arrivalLongitudeEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        departureDateEditText = view.findViewById(R.id.departureDateEditText);
        arrivalDateEditText = view.findViewById(R.id.arrivalDateEditText);

        // List of transport types
        String[] transportTypes = {"plane", "train"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                transportTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportTypeSpinner.setAdapter(adapter);

        // Set date picker for date fields
        setupDatePicker(departureDateEditText, true);
        setupDatePicker(arrivalDateEditText, false);

        view.findViewById(R.id.createTransportButton).setOnClickListener(v -> createTransport());

        return view;
    }

    private void setupDatePicker(EditText dateEditText, boolean isDeparture) {
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showDatePickerDialog(dateEditText, isDeparture);
        });
        dateEditText.setOnClickListener(v -> showDatePickerDialog(dateEditText, isDeparture));
    }

    private void showDatePickerDialog(EditText dateEditText, boolean isDeparture) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    // Show TimePickerDialog after selecting the date
                    new android.app.TimePickerDialog(
                            requireContext(),
                            (timePicker, hourOfDay, minute) -> {
                                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDate.set(Calendar.MINUTE, minute);
                                Date date = selectedDate.getTime();
                                dateEditText.setText(dateFormat.format(date));

                                if (isDeparture) {
                                    departureDate = date;
                                } else {
                                    arrivalDate = date;
                                }
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true // 24-hour format
                    ).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void createTransport() {
        try {
            String type = transportTypeSpinner.getSelectedItem().toString();
            String brand = getNonEmptyText(brandEditText, "Brand is required");
            String departureLocation = getNonEmptyText(departureLocationEditText, "Departure location is required");
            String arrivalLocation = getNonEmptyText(arrivalLocationEditText, "Arrival location is required");
            double departureLatitude = getValidDouble(departureLatitudeEditText, "Departure latitude is required");
            double departureLongitude = getValidDouble(departureLongitudeEditText, "Departure longitude is required");
            double arrivalLatitude = getValidDouble(arrivalLatitudeEditText, "Arrival latitude is required");
            double arrivalLongitude = getValidDouble(arrivalLongitudeEditText, "Arrival longitude is required");
            double price = getValidDouble(priceEditText, "Price is required");

            if (departureDate == null || arrivalDate == null) {
                Toast.makeText(getContext(), "Please select both dates", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String firebaseId = FirebaseFirestore.getInstance().collection("transports").document().getId();

            Transport transport = new Transport(
                    firebaseId, type, brand, price, departureDate, arrivalDate,
                    departureLocation, arrivalLocation, departureLatitude, departureLongitude,
                    arrivalLatitude, arrivalLongitude
            );

            FirestoreUtils.addTransport(transport, new FirestoreUtils.FirestoreCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    resetForm();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getContext(), "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getNonEmptyText(EditText editText, String errorMessage) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return text;
    }

    private double getValidDouble(EditText editText, String errorMessage) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage + " must be a valid number");
        }
    }

    private void resetForm() {
        brandEditText.setText("");
        departureLocationEditText.setText("");
        arrivalLocationEditText.setText("");
        departureLatitudeEditText.setText("");
        departureLongitudeEditText.setText("");
        arrivalLatitudeEditText.setText("");
        arrivalLongitudeEditText.setText("");
        priceEditText.setText("");
        departureDateEditText.setText("");
        arrivalDateEditText.setText("");
        departureDate = null;
        arrivalDate = null;
    }
}
