package com.example.easybooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookings;
    private final Context context;

    // Constructor to pass the data and context
    public BookingAdapter(List<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Set data to the views
        holder.bookingIdTextView.setText("Booking ID: " + booking.getBookingId());
        holder.dateRangeTextView.setText(booking.getDateRange());
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.locationTextView.setText(booking.getLocation());
        holder.carInfoTextView.setText(booking.getCarInfo() != null ? booking.getCarInfo() : "N/A");
        holder.totalAmountTextView.setText("Total: " + booking.getTotalAmount());
        holder.statusTextView.setText(booking.getStatus());

        // Set status color
        if (booking.getStatus().equalsIgnoreCase("Pending")) {
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.statusTextView.setTextColor(context.getResources().getColor(R.color.green));
        }

        // Show buttons only for Current Bookings
        if (booking.isCurrentBooking()) {
            holder.buttonLayout.setVisibility(View.VISIBLE);

            holder.cancelButton.setOnClickListener(v -> {
                Toast.makeText(context, "Booking " + booking.getBookingId() + " Cancelled", Toast.LENGTH_SHORT).show();
                // Implement your cancellation logic here
            });

            holder.checkoutButton.setOnClickListener(v -> {
                Toast.makeText(context, "Go to Checkout for " + booking.getBookingId(), Toast.LENGTH_SHORT).show();
                // Implement your checkout logic here
            });
        } else {
            holder.buttonLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // ViewHolder class
    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        private final TextView bookingIdTextView;
        private final TextView dateRangeTextView;
        private final TextView hotelNameTextView;
        private final TextView locationTextView;
        private final TextView carInfoTextView;
        private final TextView totalAmountTextView;
        private final TextView statusTextView;
        private final View buttonLayout;
        private final Button cancelButton;
        private final Button checkoutButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            bookingIdTextView = itemView.findViewById(R.id.bookingIdTextView);
            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            carInfoTextView = itemView.findViewById(R.id.carInfoTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            checkoutButton = itemView.findViewById(R.id.checkoutButton);
        }
    }
}
