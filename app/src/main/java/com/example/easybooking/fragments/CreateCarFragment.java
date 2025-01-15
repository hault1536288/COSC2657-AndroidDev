package com.example.easybooking.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.easybooking.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateCarFragment extends Fragment {
    private Uri imageUri;
    private ImageView carImageView;
    private EditText nameEditText, brandEditText, modelEditText, descriptionEditText, pricePerDayEditText, pricePerHourEditText;
    private Button uploadImageButton, createCarButton;
    private FirebaseFirestore db;
    private ActivityResultLauncher<String> imagePickerLauncher;

    public CreateCarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCloudinary();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUri = uri;
                        carImageView.setImageURI(imageUri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_car, container, false);

        db = FirebaseFirestore.getInstance();

        initializeViews(view);
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        createCarButton.setOnClickListener(v -> uploadCarData());

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
            Log.d("Cloudinary", "MediaManager already initialized");
        }
    }

    private void initializeViews(View view) {
        carImageView = view.findViewById(R.id.carImageView);
        nameEditText = view.findViewById(R.id.nameEditText);
        brandEditText = view.findViewById(R.id.brandEditText);
        modelEditText = view.findViewById(R.id.modelEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        pricePerDayEditText = view.findViewById(R.id.pricePerDayEditText);
        pricePerHourEditText = view.findViewById(R.id.pricePerHourEditText);
        uploadImageButton = view.findViewById(R.id.uploadImageButton);
        createCarButton = view.findViewById(R.id.createCarButton);
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

    private void uploadCarData() {
        if (!validateInputs()) {
            return;
        }

        AlertDialog progressDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Uploading Car Data")
                .setMessage("Please wait...")
                .setCancelable(false)
                .create();
        progressDialog.show();

        if (imageUri != null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "car_" + timestamp;

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
                                    saveCarToFirestore(imageUrl, progressDialog);
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

    private void saveCarToFirestore(@NonNull String imageUrl, @NonNull AlertDialog progressDialog) {
        try {
            Map<String, Object> carData = new HashMap<>();
            carData.put("name", nameEditText.getText().toString().trim());
            carData.put("brand", brandEditText.getText().toString().trim());
            carData.put("model", modelEditText.getText().toString().trim());
            carData.put("description", descriptionEditText.getText().toString().trim());
            carData.put("pricePerDay", Double.parseDouble(pricePerDayEditText.getText().toString()));
            carData.put("pricePerHour", Double.parseDouble(pricePerHourEditText.getText().toString()));
            carData.put("carImageUrl", imageUrl);
            carData.put("timestamp", FieldValue.serverTimestamp());

            db.collection("cars")
                    .add(carData)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        showToast("Car added successfully");
                        clearForm();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        showToast("Error adding car: " + e.getMessage());
                    });
        } catch (NumberFormatException e) {
            progressDialog.dismiss();
            showToast("Please enter valid numbers for prices");
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
        if (brandEditText.getText().toString().trim().isEmpty()) {
            showToast("Please enter brand");
            return false;
        }
        if (modelEditText.getText().toString().trim().isEmpty()) {
            showToast("Please enter model");
            return false;
        }
        if (descriptionEditText.getText().toString().trim().isEmpty()) {
            showToast("Please enter description");
            return false;
        }
        return true;
    }

    private void clearForm() {
        nameEditText.setText("");
        brandEditText.setText("");
        modelEditText.setText("");
        descriptionEditText.setText("");
        pricePerDayEditText.setText("");
        pricePerHourEditText.setText("");
        carImageView.setImageResource(R.drawable.ic_add_a_photo_24);
        imageUri = null;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}