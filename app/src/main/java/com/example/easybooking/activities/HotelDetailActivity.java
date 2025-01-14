package com.example.easybooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.easybooking.R;

public class HotelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_detail);

        String hotelImageUrl = getIntent().getStringExtra("HOTEL_IMAGE_URL");
        String hotelName = getIntent().getStringExtra("HOTEL_NAME");
        String hotelDescription = getIntent().getStringExtra("HOTEL_DESCRIPTION");
        String hotelLocation = getIntent().getStringExtra("HOTEL_LOCATION");
        double latitude = getIntent().getDoubleExtra("HOTEL_LAT", 0.0);
        double longitude = getIntent().getDoubleExtra("HOTEL_LNG", 0.0);

        ImageView hotelDetailImageView = findViewById(R.id.hotelDetailImageView);
        TextView hotelDetailNameTextView = findViewById(R.id.hotelDetailNameTextView);
        TextView hotelDetailDescriptionTextView = findViewById(R.id.hotelDetailDescriptionTextView);
        TextView hotelDetailLocationTextView = findViewById(R.id.hotelDetailLocationTextView);
        Button viewOnMapButton = findViewById(R.id.viewOnMapButton);

        Glide.with(this)
                .load(hotelImageUrl)
                .placeholder(R.drawable.ic_hotel)
                .error(R.drawable.ic_hotel)
                .into(hotelDetailImageView);

        hotelDetailNameTextView.setText(hotelName);
        hotelDetailDescriptionTextView.setText(hotelDescription);
        hotelDetailLocationTextView.setText(hotelLocation);

        viewOnMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(HotelDetailActivity.this, MapActivity.class);
            intent.putExtra("NAME", hotelName);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LNG", longitude);
            startActivity(intent);
        });
    }
}