package com.example.easybooking.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easybooking.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

public class CreateHotelFragment extends Fragment {
    private Uri imageUri;
    private ImageView hotelImageView;
    private EditText nameEditText, descriptionEditText, locationEditText,
                     latitudeEditText, longitudeEditText, 
                     pricePerNightEditText, pricePerHourEditText;
    private Button uploadImageButton, createHotelButton;
    
    private FirebaseFirestore db;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initCloudinary();
        
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    hotelImageView.setImageURI(imageUri);
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_hotel, container, false);
        
        db = FirebaseFirestore.getInstance();
        
        initializeViews(view);
        
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        createHotelButton.setOnClickListener(v -> uploadHotelData());
        
        return view;
    }

    private void initCloudinary() {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", getString(R.string.cloudinary_cloud_name));
            config.put("api_key", getString(R.string.cloudinary_api_key));
            config.put("api_secret", getString(R.string.cloudinary_api_secret));
            MediaManager.init(requireContext(), config);
        } catch (IllegalStateException e) {
            showToast("Error initializing Cloudinary: " + e.getMessage());
        }
    }

    private void initializeViews(View view) {
        hotelImageView = view.findViewById(R.id.hotelImageView);
        nameEditText = view.findViewById(R.id.nameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        latitudeEditText = view.findViewById(R.id.latitudeEditText);
        longitudeEditText = view.findViewById(R.id.longitudeEditText);
        pricePerNightEditText = view.findViewById(R.id.pricePerNightEditText);
        pricePerHourEditText = view.findViewById(R.id.pricePerHourEditText);
        uploadImageButton = view.findViewById(R.id.uploadImageButton);
        createHotelButton = view.findViewById(R.id.createHotelButton);
    }

    private void openImageChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), 
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                imagePickerLauncher.launch("image/*");
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                imagePickerLauncher.launch("image/*");
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    imagePickerLauncher.launch("image/*");
                } else {
                    showToast("Permission denied. Cannot select images.");
                }
            });

    private void uploadHotelData() {
        if (!validateInputs()) {
            return;
        }

        AlertDialog progressDialog = new AlertDialog.Builder(requireContext())
            .setTitle("Uploading Hotel Data")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create();
        progressDialog.show();

        if (imageUri != null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "hotel_" + timestamp;
            
            MediaManager.get().upload(imageUri)
                .option("public_id", fileName)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        progressDialog.setMessage("Starting upload...");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        int progress = (int) ((bytes * 100) / totalBytes);
                        progressDialog.setMessage("Uploading... " + progress + "%");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        if (resultData != null) {
                            Object urlObj = resultData.get("url");
                            if (urlObj != null) {
                                String imageUrl = urlObj.toString().replace("http:", "https:");
                                saveHotelToFirestore(imageUrl, progressDialog);
                            } else {
                                progressDialog.dismiss();
                                showToast("Upload failed: Missing URL in response");
                            }
                        } else {
                            progressDialog.dismiss();
                            showToast("Upload failed: Invalid response");
                        }
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        progressDialog.dismiss();
                        String errorMessage = error != null ? error.getDescription() : "Unknown error";
                        showToast("Upload failed: " + errorMessage);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        progressDialog.dismiss();
                        String errorMessage = error != null ? error.getDescription() : "Unknown error";
                        showToast("Upload rescheduled: " + errorMessage);
                    }
                })
                .dispatch();
        }
    }

    private void saveHotelToFirestore(@NonNull String imageUrl, @NonNull AlertDialog progressDialog) {
        try {
            double latitude = Double.parseDouble(latitudeEditText.getText().toString());
            double longitude = Double.parseDouble(longitudeEditText.getText().toString());
            GeoPoint coordinates = new GeoPoint(latitude, longitude);

            Map<String, Object> hotelData = new HashMap<>();
            hotelData.put("hotelName", nameEditText.getText().toString().trim());
            hotelData.put("description", descriptionEditText.getText().toString().trim());
            hotelData.put("location", locationEditText.getText().toString().trim());
            hotelData.put("coordinates", coordinates);
            hotelData.put("pricePerNight", Double.parseDouble(pricePerNightEditText.getText().toString()));
            hotelData.put("pricePerHour", Double.parseDouble(pricePerHourEditText.getText().toString()));
            hotelData.put("imageUrl", imageUrl);
            hotelData.put("timestamp", FieldValue.serverTimestamp());

            db.collection("hotels")
                .add(hotelData)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    showToast("Hotel added successfully");
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showToast("Error adding hotel: " + e.getMessage());
                });
        } catch (NumberFormatException e) {
            progressDialog.dismiss();
            showToast("Please enter valid numbers for prices and coordinates");
        }
    }

    private boolean validateInputs() {
        if (imageUri == null) {
            showToast("Please select an image");
            return false;
        }
        if (nameEditText.getText().toString().trim().isEmpty()) {
            showToast("Please enter hotel name");
            return false;
        }
        return true;
    }

    private void clearForm() {
        nameEditText.setText("");
        descriptionEditText.setText("");
        locationEditText.setText("");
        latitudeEditText.setText("");
        longitudeEditText.setText("");
        pricePerNightEditText.setText("");
        pricePerHourEditText.setText("");
        hotelImageView.setImageResource(R.drawable.ic_add_a_photo_24);
        imageUri = null;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}