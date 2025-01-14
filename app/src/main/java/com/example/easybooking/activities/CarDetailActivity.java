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

import java.math.BigDecimal;

public class CarDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_detail);

        String carImageUrl = getIntent().getStringExtra("CAR_IMAGE_URL");
        String carName = getIntent().getStringExtra("CAR_NAME");
        String carBrand = getIntent().getStringExtra("CAR_BRAND");
        String carModel = getIntent().getStringExtra("CAR_MODEL");
        String carDescription = getIntent().getStringExtra("CAR_DESCRIPTION");
        double carPricePerDay = getIntent().getDoubleExtra("CAR_PRICE_PER_DAY", 0.0);
        double carPricePerHour = getIntent().getDoubleExtra("CAR_PRICE_PER_HOUR", 0.0);

        ImageView carDetailImageView = findViewById(R.id.carDetailImageView);
        TextView carDetailNameTextView = findViewById(R.id.carDetailNameTextView);
        TextView carDetailBrandTextView = findViewById(R.id.carDetailBrandTextView);
        TextView carDetailModelTextView = findViewById(R.id.carDetailModelTextView);
        TextView carDetailDescriptionTextView = findViewById(R.id.carDetailDescriptionTextView);
        TextView carDetailPriceTextView = findViewById(R.id.carDetailPriceTextView);

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
    }
}