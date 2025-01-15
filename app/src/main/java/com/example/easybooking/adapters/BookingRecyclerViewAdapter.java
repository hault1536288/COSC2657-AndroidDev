package com.example.easybooking.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.activities.BookingDetailActivity;
import com.example.easybooking.activities.CarDetailActivity;
import com.example.easybooking.models.Booking;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.BookingViewHolder> {
    Context context;
    ArrayList<Booking> bookingArrayList;

    public BookingRecyclerViewAdapter(Context context, ArrayList<Booking> bookingArrayList) {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
    }

    @NonNull
    @Override
    public BookingRecyclerViewAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRecyclerViewAdapter.BookingViewHolder holder, int position) {
        // Assigns data to the views in each item in the RecyclerView
        // based on the position of the item in the list
        Booking booking = bookingArrayList.get(position);
        holder.dateRangeTextView.setText(booking.getDateRange());
        holder.bookingIdTextView.setText("Booking ID: " + booking.getBookingId());
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.locationTextView.setText(booking.getLocationRange());
        holder.carInfoTextView.setText(booking.getCarName());
        holder.totalAmountTextView.setText("Total: $" + String.valueOf(booking.getTotalAmount()));
        holder.statusTextView.setText(booking.getStatus());

        // change color based on status
        if ("pending".equalsIgnoreCase(booking.getStatus())) {
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.gray));
        } else if ("success".equalsIgnoreCase(booking.getStatus())) {
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        } else if ("cancelled".equalsIgnoreCase(booking.getStatus())) {
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        }

        if ("pending".equalsIgnoreCase(booking.getStatus())) {
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.checkoutButton.setVisibility(View.VISIBLE);
        } else {
            holder.cancelButton.setVisibility(View.GONE);
            holder.checkoutButton.setVisibility(View.GONE);
        }

        // Set up button click listener
        holder.cancelButton.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Booking")
                    .setMessage("Are you sure you want to cancel this booking?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        // Update Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("bookings")
                                .document(booking.getBookingId())
                                .update("status", "cancelled",
                                        "isCurrent", false)
                                .addOnSuccessListener(aVoid -> {
                                    // Update UI
                                    booking.setStatus("cancelled");
                                    notifyItemChanged(holder.getAdapterPosition());
                                    Toast.makeText(context, "Booking cancelled successfully.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to cancel booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Back", null)
                    .show();
        });
        holder.checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);

            // Pass data to the CarDetailActivity
            intent.putExtra("BOOKING_ID", booking.getBookingId());
            intent.putExtra("BOOKING_STATUS", booking.getStatus());
            intent.putExtra("HOTEL_NAME", booking.getHotelName());
            intent.putExtra("HOTEL_LOCATION", booking.getHotelLocation());
            intent.putExtra("HOTEL_STAY_DURATION", booking.getSpecificDateRange("hotel"));
            intent.putExtra("HOTEL_TOTAL", booking.getHotelTotal());
            intent.putExtra("TRANSPORT_TYPE", booking.getTransportType());
            intent.putExtra("TRANSPORT_BRAND", booking.getTransportBrand());
            intent.putExtra("TRANSPORT_TOTAL", booking.getTransportTotal());
            intent.putExtra("DEPARTURE_LOCATION", booking.getTransportDepartureLocation());
            intent.putExtra("DEPARTURE_TIME", booking.getTransportDepartureTime());
            intent.putExtra("DEPARTURE_DATE", booking.getTransportDepartureDay());
            intent.putExtra("ARRIVAL_LOCATION", booking.getTransportArrivalLocation());
            intent.putExtra("ARRIVAL_TIME", booking.getTransportArrivalTime());
            intent.putExtra("ARRIVAL_DATE", booking.getTransportArrivalDay());
            intent.putExtra("CAR_NAME", booking.getCarName());
            intent.putExtra("CAR_BRAND", booking.getCarBrand());
            intent.putExtra("CAR_MODEL", booking.getCarModel());
            intent.putExtra("CAR_RENT_DURATION", booking.getSpecificDateRange("car"));
            intent.putExtra("CAR_TOTAL", booking.getCarTotal());
            intent.putExtra("TOTAL_AMOUNT", booking.getTotalAmount());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Returns the number of items in the RecyclerView
        return bookingArrayList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout here (from the item_booking layout file)
        // kinda like the OnCreate method
        TextView dateRangeTextView, bookingIdTextView, hotelNameTextView, locationTextView, carInfoTextView, totalAmountTextView, statusTextView;
        Button cancelButton, checkoutButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            bookingIdTextView = itemView.findViewById(R.id.bookingIdTextView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            carInfoTextView = itemView.findViewById(R.id.carInfoTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            checkoutButton = itemView.findViewById(R.id.checkoutButton);
        }
    }
}
