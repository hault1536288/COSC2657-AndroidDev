package com.example.easybooking.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easybooking.R;

public class TransportTicketDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transport_ticket_detail);

        String transportId = getIntent().getStringExtra("TRANSPORT_ID");
        String transportType = getIntent().getStringExtra("TRANSPORT_TYPE");
        String brand = getIntent().getStringExtra("BRAND");
        String departureLocation = getIntent().getStringExtra("DEPARTURE_LOCATION");
        String departureTime = getIntent().getStringExtra("DEPARTURE_TIME");
        String departureDate = getIntent().getStringExtra("DEPARTURE_DATE");
        String arrivalLocation = getIntent().getStringExtra("ARRIVAL_LOCATION");
        String arrivalTime = getIntent().getStringExtra("ARRIVAL_TIME");
        String arrivalDate = getIntent().getStringExtra("ARRIVAL_DATE");
        double price = getIntent().getDoubleExtra("PRICE", 0.0);

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
        brandTextView.setText(brand);
        departureLocationTextView.setText(departureLocation);
        departureTimeTextView.setText(departureTime);
        departureDateTextView.setText(departureDate);
        arrivalLocationTextView.setText(arrivalLocation);
        arrivalTimeTextView.setText(arrivalTime);
        arrivalDateTextView.setText(arrivalDate);
        priceTextView.setText(String.valueOf(price));
    }
}