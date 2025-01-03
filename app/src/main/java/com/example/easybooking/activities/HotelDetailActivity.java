package com.example.easybooking.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        double hotelRating = getIntent().getDoubleExtra("HOTEL_RATING", 0.0);

        ImageView hotelDetailImageView = findViewById(R.id.hotelDetailImageView);
        TextView hotelDetailNameTextView = findViewById(R.id.hotelDetailNameTextView);
        TextView hotelDetailRatingTextView = findViewById(R.id.hotelDetailRatingTextView);
        TextView hotelDetailDescriptionTextView = findViewById(R.id.hotelDetailDescriptionTextView);
        TextView hotelDetailLocationTextView = findViewById(R.id.hotelDetailLocationTextView);

        Glide.with(this)
                .load(hotelImageUrl)
                .placeholder(R.drawable.ic_hotel)
                .error(R.drawable.ic_hotel)
                .into(hotelDetailImageView);
        hotelDetailNameTextView.setText(hotelName);
        hotelDetailRatingTextView.setText(String.valueOf(hotelRating));
        hotelDetailDescriptionTextView.setText(hotelDescription);
        hotelDetailLocationTextView.setText(hotelLocation);
    }
}