package com.example.easybooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookingList;
    private final Context context;

    public BookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
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
        Booking booking = bookingList.get(position);

        // Use context.getString() with placeholders
        holder.bookingIdTextView.setText(context.getString(R.string.booking_id, booking.getBookingId()));
        holder.dateRangeTextView.setText(booking.getDateRange());
        holder.hotelNameTextView.setText(context.getString(R.string.hotel_name, booking.getHotelName()));
        holder.locationTextView.setText(context.getString(R.string.location, booking.getLocation()));
        holder.totalAmountTextView.setText(context.getString(R.string.total_amount, booking.getTotalAmount()));
        holder.statusTextView.setText(booking.getStatus());

        // Show buttons for current bookings
        if (booking.isCurrentBooking()) {
            holder.buttonLayout.setVisibility(View.VISIBLE);
        } else {
            holder.buttonLayout.setVisibility(View.GONE);
        }

        // Set colors for status
        if (booking.getStatus().equalsIgnoreCase("Success")) {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdTextView, dateRangeTextView, hotelNameTextView, locationTextView, totalAmountTextView, statusTextView;
        LinearLayout buttonLayout;
        Button cancelButton, checkoutButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingIdTextView = itemView.findViewById(R.id.bookingIdTextView);
            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            checkoutButton = itemView.findViewById(R.id.checkoutButton);
        }
    }
}
